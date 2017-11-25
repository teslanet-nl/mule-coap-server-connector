package nl.teslanet.mule.connectors.coap.server;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.OptionSet;
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

    private static final Object PROPNAME_COAP_RESOURCECHANGED= null;

    private CoapServerConnector connector;

    private Resource configuredresource;

    public ServedResource( CoapServerConnector coapServerConnector, Resource resource )
    {
        super( resource.getPath() );
        connector= coapServerConnector;
        configuredresource= resource;
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
            Object response= null;
            ResponseCode responseCode= ResponseCode.CONTENT;
            
            if ( configuredresource.isSendAccept())
            {
                exchange.accept();
            }
            String requestPayload= exchange.getRequestText();
            Map< String, Object > inboundProperties= handleProperties( exchange );
            Map< String, Object > outboundProperties= new HashMap< String, Object >();

            try
            {
                response= processMuleFlow( inboundProperties, requestPayload, outboundProperties );
            }
            catch ( Exception e )
            {
                //TODO make adequate ERROR!
                exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
            }
            if ( outboundProperties.containsKey( PROPNAME_COAP_RESPONSE_CODE ))
            {
                responseCode= ResponseCode.valueOf( outboundProperties.get( PROPNAME_COAP_RESPONSE_CODE ).toString());
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
            if ( configuredresource.isSendAccept())
            {
                exchange.accept();
            }
            String requestPayload= exchange.getRequestText();
            Map< String, Object > inboundProperties= handleProperties( exchange );
            Map< String, Object > outboundProperties= new HashMap< String, Object >();

            try
            {
                response= processMuleFlow( inboundProperties, requestPayload, outboundProperties );
            }
            catch ( Exception e )
            {
                //TODO make adequate ERROR!
                exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
            }
            if ( outboundProperties.containsKey( PROPNAME_COAP_RESPONSE_CODE ))
            {
                responseCode= ResponseCode.valueOf( outboundProperties.get( PROPNAME_COAP_RESPONSE_CODE ).toString());
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
        } ;
    }

    @Override
    public void handlePOST( CoapExchange exchange )
    {
        if ( !configuredresource.isPut() )
        {
            //default implementation is METHOD_NOT_ALLOWED
            super.handlePUT( exchange );
        }
        else
        {
            Object response= null;
            ResponseCode responseCode= ResponseCode.CHANGED;
            if ( configuredresource.isSendAccept())
            {
                exchange.accept();
            }            
            String requestPayload= exchange.getRequestText();
            Map< String, Object > inboundProperties= handleProperties( exchange );
            Map< String, Object > outboundProperties= new HashMap< String, Object >();

            try
            {
                response= processMuleFlow( inboundProperties, requestPayload, outboundProperties );
            }
            catch ( Exception e )
            {
                //TODO make adequate ERROR!
                exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR );
            }
            if ( outboundProperties.containsKey( PROPNAME_COAP_RESPONSE_CODE ))
            {
                responseCode= ResponseCode.valueOf( outboundProperties.get( PROPNAME_COAP_RESPONSE_CODE ).toString());
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
        } ;
    }

    private Map< String, Object > handleProperties( CoapExchange exchange )
    {
        //Option o;
        HashMap< String, Object > props= new HashMap< String, Object >();
        OptionSet options= exchange.getRequestOptions();
        props.put( "coap.request.options.locationpath", options.getLocationPathString() );
        props.put( "coap.request.options.locationquery", options.getLocationQueryString() );
        props.put( "coap.request.options.location", options.getLocationString() );
        props.put( "coap.request.options.observe", options.getObserve() );
        props.put( "coap.request.options.maxage", options.getMaxAge() );
        props.put( "coap.request.options.others",  options.getOthers() );
        props.put( "coap.request.options.contentformat", options.getContentFormat() );
        props.put( "coap.request.options.accept", options.getAccept() );
        props.put( "coap.request.code", exchange.getRequestCode().toString() );

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
