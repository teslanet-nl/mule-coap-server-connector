package nl.teslanet.mule.connectors.coap.server;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.mule.DefaultMuleEvent;
import org.mule.DefaultMuleMessage;
import org.mule.MessageExchangePattern;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.security.oauth.processor.AbstractListeningMessageProcessor;


public class ServedResource extends CoapResource
{
    private static final Object PROPNAME_COAP_RESPONSE_CODE= "coap.response.code";

    private CoapServerConnector connector;

    private Resource configuredresource;

    public ServedResource( CoapServerConnector coapServerConnector, Resource resource )
    {
        super( resource.getName() );
        connector= coapServerConnector;
        configuredresource= resource;
        setObservable( configuredresource.isObservable() );

        // set display name
        getAttributes().setTitle( "Mule CoAP Resource" );
    }

    @Override
    public void handleGET( CoapExchange exchange )
    {
        if ( !configuredresource.isGet() )
        {
            //default implementation is METHOD_NOT_ALLOWED
            super.handleGET( exchange );
        }
        else
        {
            Object outboundPayload= null;
            ResponseCode responseCode= ResponseCode.CONTENT;

            if ( configuredresource.isDelayedResponse() )
            {
                exchange.accept();
            }
            
            String requestPayload= exchange.getRequestText();
            Map< String, Object > inboundProperties= createInboundProperties( exchange );
            Map< String, Object > outboundProperties= new HashMap< String, Object >();

            try
            {
                
                outboundPayload= processMuleFlow( inboundProperties, requestPayload, outboundProperties );

                if ( outboundProperties.containsKey( PROPNAME_COAP_RESPONSE_CODE ) )
                {
                    responseCode= ResponseCode.valueOf( outboundProperties.get( PROPNAME_COAP_RESPONSE_CODE ).toString() );
                }
                Response resp= Response.createResponse(exchange.advanced().getRequest(), responseCode );
                if ( exchange.advanced().getRelation() != null )
                {
                    resp.setConfirmable( true );
                }
                if ( outboundPayload == null )
                {
                    //TODO is this adequate?
                    exchange.respond( resp );
                }
                else
                {
                    resp.setPayload( outboundPayload.toString() );
                    exchange.respond( resp );

                } ;
            }
            catch ( Exception e )
            {
                //TODO make adequate ERROR!
                exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
            }

        } ;
    }

    @Override
    public void handlePUT( CoapExchange exchange )
    {
        if ( !configuredresource.isPut() )
        {
            //default implementation is METHOD_NOT_ALLOWED
            super.handlePUT( exchange );
        }
        else
        {
            Object response= null;
            ResponseCode responseCode= ResponseCode.CREATED;
            if ( configuredresource.isDelayedResponse() )
            {
                exchange.accept();
            }
            String requestPayload= exchange.getRequestText();
            Map< String, Object > inboundProperties= createInboundProperties( exchange );
            Map< String, Object > outboundProperties= new HashMap< String, Object >();

            try
            {
                response= processMuleFlow( inboundProperties, requestPayload, outboundProperties );
                if ( outboundProperties.containsKey( PROPNAME_COAP_RESPONSE_CODE ) )
                {
                    responseCode= ResponseCode.valueOf( outboundProperties.get( PROPNAME_COAP_RESPONSE_CODE ).toString() );
                }
                if ( response == null )
                {
                    //TODO is this adequate?
                    exchange.respond( responseCode, "" );
                }
                else
                {
                    exchange.respond( responseCode, response.toString() );
                } ;
            }
            catch ( Exception e )
            {
                //TODO make adequate ERROR!
                exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
            }

        } ;
    }

    @Override
    public void handlePOST( CoapExchange exchange )
    {
        if ( !configuredresource.isPost() )
        {
            //default implementation is METHOD_NOT_ALLOWED
            super.handlePOST( exchange );
        }
        else
        {
            Object response= null;
            ResponseCode responseCode= ResponseCode.CHANGED;
            if ( configuredresource.isDelayedResponse() )
            {
                exchange.accept();
            }
            String requestPayload= exchange.getRequestText();
            Map< String, Object > inboundProperties= createInboundProperties( exchange );
            Map< String, Object > outboundProperties= new HashMap< String, Object >();

            try
            {
                response= processMuleFlow( inboundProperties, requestPayload, outboundProperties );
                if ( outboundProperties.containsKey( PROPNAME_COAP_RESPONSE_CODE ) )
                {
                    responseCode= ResponseCode.valueOf( outboundProperties.get( PROPNAME_COAP_RESPONSE_CODE ).toString() );
                }
                if ( response == null )
                {
                    //TODO is this adequate?
                    exchange.respond( responseCode, "" );
                }
                else
                {
                    exchange.respond( responseCode, response.toString() );
                } ;
            }
            catch ( Exception e )
            {
                //TODO make adequate ERROR!
                exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
            }

        } ;
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

        OptionSet options= exchange.getRequestOptions();
        props.put( "coap.request.options.locationpath", options.getLocationPathString() );
        props.put( "coap.request.options.locationquery", options.getLocationQueryString() );
        props.put( "coap.request.options.location", options.getLocationString() );
        props.put( "coap.request.options.observe", options.getObserve() );
        props.put( "coap.request.options.maxage", options.getMaxAge() );
        props.put( "coap.request.options.others", options.getOthers() );
        props.put( "coap.request.options.contentformat", options.getContentFormat() );
        props.put( "coap.request.options.accept", options.getAccept() );

        return props;
    }

    private Object processMuleFlow( Map< String, Object > inboundProperties, Object requestPayload, Map< String, Object > outboundProperties ) throws MuleException
    {
        Object response= null;
        //TODO make safe:
        AbstractListeningMessageProcessor processor= (AbstractListeningMessageProcessor) connector.getCallback();
        MuleMessage muleMessage;
        muleMessage= new DefaultMuleMessage( requestPayload, inboundProperties, null, null, processor.getMuleContext() );
        MuleEvent muleEvent;
        muleEvent= new DefaultMuleEvent( muleMessage, MessageExchangePattern.REQUEST_RESPONSE, processor.getFlowConstruct() );

        MuleEvent responseEvent;
        responseEvent= processor.processEvent( muleEvent );
        if ( ( responseEvent != null ) && ( responseEvent.getMessage() != null ) )
        {
            response= responseEvent.getMessage().getPayload();
            for ( String propName : responseEvent.getMessage().getOutboundPropertyNames() )
            {
                outboundProperties.put( propName, responseEvent.getMessage().getOutboundProperty( propName ) );
            } ;
        }
        return response;
    };

}
