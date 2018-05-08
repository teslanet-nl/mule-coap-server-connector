/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Public License - v 2.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.server;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.Resource;
import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.callback.SourceCallback;
import org.mule.processor.NullMessageProcessor;
import org.mule.security.oauth.processor.AbstractListeningMessageProcessor;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.transport.NullPayload;

import nl.teslanet.mule.transport.coap.commons.options.Options;
import nl.teslanet.mule.transport.coap.commons.options.PropertyNames;
import nl.teslanet.mule.transport.coap.server.config.ResourceConfig;

//TODO: processors should pass through message, not null

public class ServedResource extends CoapResource
{

    /** The logger. */
    protected final Logger LOGGER = Logger.getLogger(ServedResource.class.getCanonicalName());

    private CoapServerConnector connector;

    private ResourceConfig config;

    private SourceCallback callback= null;

    public ServedResource( CoapServerConnector coapServerConnector, ResourceConfig resourceConfig )
    {
        super( resourceConfig.getName() );
        connector= coapServerConnector;
        config= resourceConfig;
        setObservable( config.isObserve() );

        //TODO make use of visible/invisible?
        // set display name
        //TODO make title attribute in resourceconfig
        //TODO add more attributes (e.g. types)
        getAttributes().setTitle( "Mule CoAP Resource " + getName() );
        if ( config.getType() != null )
        {
            getAttributes().setAttribute( LinkFormat.CONTENT_TYPE, MediaTypeRegistry.toString( MediaTypeRegistry.parse( config.getType() ) ) );
        }
        if ( config.getSize() != null )
        {
            getAttributes().setMaximumSizeEstimate( config.getSize() );
        }
        if ( config.isObserve() ) getAttributes().setObservable();
    }

    @Override
    public void handleGET( CoapExchange exchange )
    {
        if ( !config.isGet() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handleGET( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.CONTENT );
        }
    }

    @Override
    public void handlePUT( CoapExchange exchange )
    {
        if ( !config.isPut() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handlePUT( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.CREATED );
        }
    }

    @Override
    public void handlePOST( CoapExchange exchange )
    {
        if ( !config.isPost() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handlePOST( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.CHANGED );
        }
    }

    @Override
    public void handleDELETE( CoapExchange exchange )
    {
        if ( !config.isDelete() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handleDELETE( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.DELETED );
        }
    }

    private void handleRequest( CoapExchange exchange, ResponseCode defaultResponseCode )
    {
        Object outboundPayload= null;
        ResponseCode responseCode= defaultResponseCode;
        if ( !hasCallback() )
        {
            exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR, "NO LISTENER" );
        }

        if ( config.isEarlyAck() )
        {
            exchange.accept();
        }
        byte[] requestPayload= exchange.getRequestPayload();
        int requestContentFormat= exchange.getRequestOptions().getContentFormat();

        Map< String, Object > inboundProperties= createInboundProperties( exchange );
        Map< String, Object > outboundProperties= new HashMap< String, Object >();
        try
        {
            outboundPayload= processMuleFlow( requestPayload, requestContentFormat, inboundProperties, outboundProperties );
            if ( outboundProperties.containsKey( PropertyNames.COAP_RESPONSE_CODE ) )
            {
                responseCode= ResponseCode.valueOf( outboundProperties.get( PropertyNames.COAP_RESPONSE_CODE ).toString() );
            }
            Response response= new Response( responseCode );
            Options options= new Options( outboundProperties );
            response.setOptions( options );

            //if ( exchange.advanced().getRelation() != null  )
            //{
            // is an observe response
            //}
            //TODO check responsecode allows for content
            if ( byte[].class.isInstance( outboundPayload ) )
            {
                response.setPayload( (byte[]) outboundPayload );
            }
            else if ( outboundPayload != null && ! NullPayload.getInstance().equals( outboundPayload ))
            {
                response.setPayload( outboundPayload.toString() );
            } ;
            exchange.respond( response );
        }
        catch ( Exception e )
        {
            //TODO make adequate ERROR! Mule exception strategy has not kicked in yet, and does not...
            LOGGER.log( Level.SEVERE, "CoAP: failed to process request failed: " + exchange.advanced().getRequest().getURI(), e );
            exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
        }
    }

    private Map< String, Object > createInboundProperties( CoapExchange exchange )
    {

        HashMap< String, Object > props= new HashMap< String, Object >();

        props.put( "coap.request.code", exchange.getRequestCode().toString() );
        props.put( "coap.request.adress", exchange.advanced().getEndpoint().getAddress() );
        props.put( "coap.request.uri", this.getURI() );
        props.put( "coap.request.relation", ( exchange.advanced().getRelation() != null ? exchange.advanced().getRelation().getKey() : null ) );
        props.put( "coap.request.source.host", exchange.getSourceAddress() );
        props.put( "coap.request.source.port", exchange.getSourcePort() );

        Options.fillProperties( exchange.getRequestOptions(), props );

        return props;
    }

    private Object processMuleFlow( Object requestPayload, int requestContentFormat, Map< String, Object > inboundProperties, Map< String, Object > outboundProperties )
        throws MuleException
    {
        Object response= null;

        AbstractListeningMessageProcessor processor= (AbstractListeningMessageProcessor) getCallback();

        //TODO make safe:
        MuleMessage muleMessage;
        if ( requestContentFormat == MediaTypeRegistry.UNDEFINED )
        {
            muleMessage= new DefaultMuleMessage( requestPayload, inboundProperties, null, null, processor.getMuleContext() );
        }
        else
        {
            muleMessage= new DefaultMuleMessage(
                requestPayload,
                inboundProperties,
                null,
                null,
                processor.getMuleContext(),
                DataTypeFactory.create( requestPayload.getClass(), MediaTypeRegistry.toString( requestContentFormat ) ) );
        }
        MuleEvent muleEvent= new DefaultMuleEvent( muleMessage, MessageExchangePattern.REQUEST_RESPONSE, processor.getFlowConstruct() );

        MuleEvent responseEvent= processor.processEvent( muleEvent );
        if ( ( responseEvent != null ) )
        {
            MuleMessage responseMessage= responseEvent.getMessage();
            if ( responseMessage != null )
            {
                response= responseMessage.getPayload();
                for ( String propName : responseEvent.getMessage().getOutboundPropertyNames() )
                {
                    outboundProperties.put( propName, responseEvent.getMessage().getOutboundProperty( propName ) );
                } ;

                if ( !outboundProperties.containsKey( PropertyNames.COAP_OPT_CONTENTFORMAT ) )
                {
                    String mimeType= responseMessage.getDataType().getMimeType();
                    outboundProperties.put( PropertyNames.COAP_OPT_CONTENTFORMAT, MediaTypeRegistry.parse( mimeType ) );
                }
            } ;
        }

        return response;
    };

    /**
     * @return get the parent resource
     */
    public ServedResource getParent()
    {
        Resource parent= super.getParent();
        if ( connector.isRootResource( parent ) )
        {
            return null;
        }
        else
        {
            return (ServedResource) parent;
        }
    }

    /**
     * @return the configured Resource
     */
    public ResourceConfig getConfiguredResource()
    {
        return config;
    }

    /**
     * set the resource specific callback
     */
    public void setCallback( SourceCallback callback )
    {
        this.callback= callback;
    }

    /**
     * @return the callback
     */
    public SourceCallback getCallback()
    {
        return callback;
    }

    public boolean hasCallback()
    {
        return( callback != null );
    }

}
