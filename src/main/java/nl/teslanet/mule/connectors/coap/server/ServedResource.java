package nl.teslanet.mule.connectors.coap.server;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
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

import nl.teslanet.mule.connectors.coap.options.OptionSet;
import nl.teslanet.mule.connectors.coap.server.config.ResourceConfig;


public class ServedResource extends CoapResource
{
    private static final String PROPNAME_COAP_RESPONSE_CODE= "coap.response.code";

    private CoapServerConnector connector;

    private ResourceConfig configuredResource;

    private SourceCallback callback= null;

    public ServedResource( CoapServerConnector coapServerConnector, ResourceConfig resourceConfig )
    {
        super( resourceConfig.getName() );
        connector= coapServerConnector;
        configuredResource= resourceConfig;
        setObservable( configuredResource.isObservable() );

        // set display name
        getAttributes().setTitle( "Mule CoAP Resource" );
    }

    @Override
    public void handleGET( CoapExchange exchange )
    {
        if ( !configuredResource.isGet() )
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
        if ( !configuredResource.isPut() )
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
        if ( !configuredResource.isPost() )
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
        if ( !configuredResource.isDelete() )
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
        if ( !hasCallback())
        {
            exchange.respond( ResponseCode.INTERNAL_SERVER_ERROR, "NO LISTENER" );
        }

        if ( configuredResource.isDelayedResponse() )
        {
            exchange.accept();
        }
        //TODO reconsider payload type: string/byte[]
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
            Response response= new Response( responseCode );
            OptionSet options= new OptionSet( outboundProperties );
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
            else if ( outboundPayload != null )
            {
                response.setPayload( outboundPayload.toString() );
            } ;
            exchange.respond( response );
        }
        catch ( Exception e )
        {
            //TODO make adequate ERROR!
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

        OptionSet.fillProperties( exchange.getRequestOptions(), props );

        return props;
    }

    private Object processMuleFlow( Map< String, Object > inboundProperties, Object requestPayload, Map< String, Object > outboundProperties ) throws MuleException
    {
        Object response= null;
        //TODO make safe:
        AbstractListeningMessageProcessor processor= (AbstractListeningMessageProcessor) getCallback();
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
        return configuredResource;
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
