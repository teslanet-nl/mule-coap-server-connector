/*******************************************************************************
 * Copyright (c) 2017, 2018, 2019 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution.
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


/**
 * The ServedResource class represents a CoAP resource that is active on the server.
 * A ServedResource object handles all requests from clients on the CoAP resource.
 */
public class ServedResource extends CoapResource
{
    //TODO review logging
    /** The logger. */
    protected final Logger LOGGER= Logger.getLogger( ServedResource.class.getCanonicalName() );

    /**
     * The callback of the messagesource.
     * It is used to handle messages over to the Mule flow that should process the request.
     */
    private SourceCallback callback= null;

    /**
     * Flag that indicates whether the resource accepts Get requests.
     */
    private Boolean get= false;

    /**
     * Flag that indicates whether the resource accepts Put requests.
     */
    private Boolean put= false;

    /**
     * Flag that indicates whether the resource accepts Post requests.
     */
    private Boolean post= false;

    /**
     * Flag that indicates whether the resource accepts Delete requests.
     */
    private Boolean delete= false;

    /**
     * Flag that indicates whether the resource should acknowledge before processing the request.
     */
    private Boolean earlyAck= false;

    /**
     * Constuctor that creates a ServedResource object according to given configuration.
     * The ServedResource and its child resources will be constructed.
     * @param resourceConfig the description of the resource to create. 
     */
    public ServedResource( ResourceConfig resourceConfig )
    {
        super( resourceConfig.getName() );

        //TODO make use of visible/invisible?

        get= resourceConfig.isGet();
        put= resourceConfig.isPut();
        post= resourceConfig.isPost();
        delete= resourceConfig.isDelete();
        earlyAck= resourceConfig.isEarlyAck();

        setObservable( resourceConfig.isObservable() );
        if ( resourceConfig.isObservable())
        {
            setObservable( true );
            getAttributes().setObservable();
        }
        else
        {
            setObservable( false );
        }

        if ( resourceConfig.getTitle() != null )
        {
            getAttributes().setTitle( resourceConfig.getTitle() );
        } ;
        if ( resourceConfig.getRt() != null )
        {
            for ( String rt : resourceConfig.getRt().split( "\\s*,\\s*" ) )
            {
                getAttributes().addResourceType( rt );
            }
        } ;
        if ( resourceConfig.getIfdesc() != null )
        {
            for ( String ifdesc : resourceConfig.getIfdesc().split( "\\s*,\\s*" ) )
            {
                getAttributes().addInterfaceDescription( ifdesc );
            }
        } ;
        if ( resourceConfig.getCt() != null )
        {
            for ( String ct : resourceConfig.getCt().split( "\\s*,\\s*" ) )
            {
                getAttributes().addContentType( Integer.parseInt( ct ) );
            }
        }
        if ( resourceConfig.getSz() != null )
        {
            getAttributes().setMaximumSizeEstimate( resourceConfig.getSz() );
        }
        //also create children (recursively) 
        for ( ResourceConfig childResourceConfig : resourceConfig.getResourceCollection() )
        {
            ServedResource child= new ServedResource( childResourceConfig );
            add( child );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.californium.core.CoapResource#handleGET(org.eclipse.californium.core.server.resources.CoapExchange)
     */
    @Override
    public void handleGET( CoapExchange exchange )
    {
        if ( !isGet() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handleGET( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.CONTENT );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.californium.core.CoapResource#handlePUT(org.eclipse.californium.core.server.resources.CoapExchange)
     */
    @Override
    public void handlePUT( CoapExchange exchange )
    {
        if ( !isPut() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handlePUT( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.CREATED );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.californium.core.CoapResource#handlePOST(org.eclipse.californium.core.server.resources.CoapExchange)
     */
    @Override
    public void handlePOST( CoapExchange exchange )
    {
        if ( !isPost() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handlePOST( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.CHANGED );
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.californium.core.CoapResource#handleDELETE(org.eclipse.californium.core.server.resources.CoapExchange)
     */
    @Override
    public void handleDELETE( CoapExchange exchange )
    {
        if ( !isDelete() )
        {
            //default implementation is to respond METHOD_NOT_ALLOWED
            super.handleDELETE( exchange );
        }
        else
        {
            handleRequest( exchange, ResponseCode.DELETED );
        }
    }

    /**
     * Generic handler for processing requests.
     * @param exchange the CoAP exchange context of the request.
     * @param defaultResponseCode the response code that will be used when the Mule flow hasn't set one.
     */
    private void handleRequest( CoapExchange exchange, ResponseCode defaultResponseCode )
    {
        if ( !hasCallback() )
        {
            exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR, "NO LISTENER" );
            return;
        }

        Object outboundPayload= null;
        ResponseCode responseCode= defaultResponseCode;

        if ( isEarlyAck() )
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
                Options.fillOptionSet( response.getOptions(), outboundProperties, false );
                //TODO setOptions uses copyconstructor of OptionSet that does not copy size1 and size2 option (bug)
                //using copyconstructor is inefficient anyhow
                //Options options= new Options( outboundProperties );
                //response.setOptions( options.getOptionSet() );

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

    /**
     * Create a map of properties that should be set on the inbound scope of the Mule Message
     * @param exchange the context of the CoAP request
     * @return map of properties
     * @throws InvalidOptionValueException when an option has an invalid value
     */
    private Map< String, Object > createInboundProperties( CoapExchange exchange ) throws InvalidOptionValueException
    {

        HashMap< String, Object > props= new HashMap< String, Object >();

        props.put( PropertyNames.COAP_REQUEST_CODE, exchange.getRequestCode().toString() );
        props.put( PropertyNames.COAP_REQUEST_CONFIRMABLE, exchange.advanced().getRequest().isConfirmable() );
        //TODO: separate host + port String in stead of address
        props.put( PropertyNames.COAP_REQUEST_ADDRESS, exchange.advanced().getEndpoint().getAddress().toString() );
        props.put( PropertyNames.COAP_REQUEST_URI, this.getURI() );
        props.put( PropertyNames.COAP_REQUEST_RELATION, ( exchange.advanced().getRelation() != null ? exchange.advanced().getRelation().getKey() : null ) );
        props.put( PropertyNames.COAP_REQUEST_SOURCE_HOST, exchange.getSourceAddress().toString() );
        props.put( PropertyNames.COAP_REQUEST_SOURCE_PORT, exchange.getSourcePort() );

        Options.fillPropertyMap( exchange.getRequestOptions(), props );

        return props;
    }

    /**
     * Hands the request over to the Mule flow to get processed.
     * @param requestPayload the request payload that will be set as Mule message payload 
     * @param requestContentFormat the content format of the request payload
     * @param inboundProperties map of properties that will be set in the inbound scope of the Mule message handed to the flow
     * @param outboundProperties map that gets filled with the outbound properties from the returned Mule message 
     * @return the result message payload.
     */
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

        if ( responseEvent != null && responseEvent.getMessage() != null && responseEvent.getMessage().getExceptionPayload() == null )
        {
            MuleMessage responseMessage= responseEvent.getMessage();
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
        }
        else
        {
            outboundProperties.put( PropertyNames.COAP_RESPONSE_CODE, "INTERNAL_SERVER_ERROR" );
            response= new String( "EXCEPTION IN PROCESSING FLOW" );
        }

        return response;
    };

    /**
     * @return the get flag
     */
    public Boolean isGet()
    {
        return get;
    }

    /**
     * @return the put flag
     */
    public Boolean isPut()
    {
        return put;
    }

    /**
     * @return the post flag
     */
    public Boolean isPost()
    {
        return post;
    }

    /**
     * @return the delete flag
     */
    public Boolean isDelete()
    {
        return delete;
    }

    /**
     * @return the earlyAck flag
     */
    public Boolean isEarlyAck()
    {
        return earlyAck;
    }

    /**
     * set the Mule callback for this resource.
     */
    public void setCallback( SourceCallback callback )
    {
        this.callback= callback;
    }

    /**
     * Get the Mule MessageSource callback
     * @return the callback
     */
    public SourceCallback getCallback()
    {
        return callback;
    }

    /**
     * Test whether the resource callback has been set.
     * @return true when callback is set.
     */
    public boolean hasCallback()
    {
        return( callback != null );
    }
}
