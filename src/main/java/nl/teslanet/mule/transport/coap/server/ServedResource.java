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
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
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
import org.mule.security.oauth.processor.AbstractListeningMessageProcessor;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.transport.NullPayload;

import nl.teslanet.mule.transport.coap.commons.options.InvalidOptionValueException;
import nl.teslanet.mule.transport.coap.commons.options.Options;
import nl.teslanet.mule.transport.coap.commons.options.PropertyNames;
import nl.teslanet.mule.transport.coap.server.config.ResourceConfig;


public class ServedResource extends CoapResource
{

    /** The logger. */
    protected final Logger LOGGER= Logger.getLogger( ServedResource.class.getCanonicalName() );

    private CoapServerConnector connector;

    private ResourceConfig config;

    private SourceCallback callback= null;

    public ServedResource( CoapServerConnector coapServerConnector, ResourceConfig resourceConfig )
    {
        super( resourceConfig.getName() );
        connector= coapServerConnector;
        config= resourceConfig;
        setObservable( config.isObservable() );

        //TODO make use of visible/invisible?

        if ( config.getTitle() != null )
        {
            getAttributes().setTitle( config.getTitle() );
        } ;
        if ( config.getRt() != null )
        {
            for ( String rt : config.getRt().split( "\\s*,\\s*" ) )
            {
                getAttributes().addResourceType( rt );
            }
        } ;
        if ( config.getIfdesc() != null )
        {
            for ( String ifdesc : config.getIfdesc().split( "\\s*,\\s*" ) )
            {
                getAttributes().addInterfaceDescription( ifdesc );
            }
        } ;
        if ( config.getCt() != null )
        {
            for ( String ct : config.getCt().split( "\\s*,\\s*" ) )
            {
                getAttributes().addContentType( Integer.parseInt( ct ) );
            }
        }
        if ( config.getSz() != null )
        {
            getAttributes().setMaximumSizeEstimate( config.getSz() );
        }
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
        if ( !hasCallback() )
        {
            exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR, "NO LISTENER" );
            return;
        }

        Object outboundPayload= null;
        ResponseCode responseCode= defaultResponseCode;

        if ( config.isEarlyAck() )
        {
            exchange.accept();
        }
        byte[] requestPayload= exchange.getRequestPayload();
        int requestContentFormat= exchange.getRequestOptions().getContentFormat();

        try
        {
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
                response.setOptions( options.getOptionSet() );

                if ( byte[].class.isInstance( outboundPayload ) )
                {
                    response.setPayload( (byte[]) outboundPayload );
                }
                else if ( outboundPayload != null && !NullPayload.getInstance().equals( outboundPayload ) )
                {
                    response.setPayload( outboundPayload.toString() );
                } ;
                exchange.respond( response );
            }
            catch ( Exception e )
            {
                throw new RuntimeException( "failed to process request: " + exchange.advanced().getRequest().getURI(), e );
            }
        }
        catch ( InvalidOptionValueException e1 )
        {
            //cannot process request when option not valid, 
            //probably Californium has already dealt with this, so shouldn't occur
            Response response= new Response( ResponseCode.BAD_OPTION );
            response.setPayload( e1.getMessage() );
            exchange.respond( response );
        }

    }

    private Map< String, Object > createInboundProperties( CoapExchange exchange ) throws InvalidOptionValueException
    {

        HashMap< String, Object > props= new HashMap< String, Object >();

        props.put( PropertyNames.COAP_REQUEST_CODE, exchange.getRequestCode().toString() );
        props.put( PropertyNames.COAP_REQUEST_CONFIRMABLE, exchange.advanced().getRequest().isConfirmable() );
        props.put( PropertyNames.COAP_REQUEST_ADDRESS, exchange.advanced().getEndpoint().getAddress() );
        props.put( PropertyNames.COAP_REQUEST_URI, this.getURI() );
        props.put( PropertyNames.COAP_REQUEST_RELATION, ( exchange.advanced().getRelation() != null ? exchange.advanced().getRelation().getKey() : null ) );
        props.put( PropertyNames.COAP_REQUEST_SOURCE_HOST, exchange.getSourceAddress() );
        props.put( PropertyNames.COAP_REQUEST_SOURCE_PORT, exchange.getSourcePort() );

        Options.fillPropertyMap( exchange.getRequestOptions(), props );

        return props;
    }

    private Object processMuleFlow( Object requestPayload, int requestContentFormat, Map< String, Object > inboundProperties, Map< String, Object > outboundProperties )

    {
        MuleEvent responseEvent= null;
        Object response= null;

        AbstractListeningMessageProcessor processor= (AbstractListeningMessageProcessor) getCallback();

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
        try
        {
            responseEvent= processor.processEvent( muleEvent );
        }
        catch ( MuleException ex )
        {
            //handle over to Flow's exception handling
            responseEvent= processor.getFlowConstruct().getExceptionListener().handleException( ex, muleEvent );
            //exception.getEvent().getFlowConstruct().getExceptionListener().handleException(exception, exception.getEvent());
            //connector.getContext().getExceptionListener().handleException( new MessagingException( muleEvent, ex ));
        }

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
