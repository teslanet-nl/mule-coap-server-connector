package nl.teslanet.mule.transport.coap.server.test.blockwise;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint.CoapEndpointBuilder;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import nl.teslanet.mule.transport.coap.server.test.utils.Data;


public abstract class AbstractBlockwiseTest extends FunctionalMunitSuite
{
    CoapClient client= null;

    protected AtomicBoolean spyActivated= new AtomicBoolean();

    private static ArrayList< Code > inboundCalls;

    private static ArrayList< Code > outboundCalls;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/blockwise/testserver1.xml";
    };

    @Override
    protected boolean haveToDisableInboundEndpoints()
    {
        return false;
    }

    @Override
    protected boolean haveToMockMuleConnectors()
    {
        return false;
    }

    @Before
    public void setUp() throws Exception
    {
        inboundCalls= new ArrayList< Code >();
        inboundCalls.add( Code.GET );
        inboundCalls.add( Code.PUT );
        inboundCalls.add( Code.POST );
        inboundCalls.add( Code.DELETE );
        outboundCalls= new ArrayList< Code >();
        outboundCalls.add( Code.GET );
        outboundCalls.add( Code.PUT );
        outboundCalls.add( Code.POST );
        outboundCalls.add( Code.DELETE );
    }

    @After
    public void tearDown() throws Exception
    {
        if ( client != null ) client.shutdown();
    }

    protected URI getURI() throws URISyntaxException
    {
        return new URI( "coap", "127.0.0.1", null, null );
    }

    abstract protected String getPath( Code call );

    protected int getContentSize()
    {
        return 10;
    }

    protected int getClientMaxContentSize()
    {
        return 8192;
    }

    protected void validateInboundResponse( Code call, CoapResponse response )
    {
        boolean activated= spyActivated.get();

        assertNotNull( "no response on: " + call, response );
        assertTrue( "response indicates failure on : " + call + " response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
        assertTrue( "spy was not activated on: " + call, activated );
    }

    protected void validateOutboundResponse( Code call, CoapResponse response )
    {
        assertNotNull( "no response on: " + call, response );
        assertTrue( "response indicates failure on : " + call + " response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
        assertTrue( "wrong payload in response on: " + call, Data.validateContent( response.getPayload(), getContentSize() ) );
    }

    private CoapClient getClient( String path ) throws URISyntaxException
    {
        CoapClient client= new CoapClient( getURI().resolve( path ) );
        client.setTimeout( 2000L );
        NetworkConfig config= NetworkConfig.createStandardWithoutFile();
        config.setInt( NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, getClientMaxContentSize() );
        CoapEndpointBuilder endpointBuilder= new CoapEndpointBuilder();
        endpointBuilder.setNetworkConfig( config );
        client.setEndpoint( endpointBuilder.build() );
        return client;
    }

    private void mockResponseMessage()
    {
        MuleMessage messageToBeReturned= muleMessageWithPayload( Data.getContent( getContentSize() ) );
        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        mocker.thenReturn( messageToBeReturned );
    }

    private void spyRequestMessage()
    {
        SpyProcess beforeSpy= new SpyProcess()
            {
                @Override
                public void spy( MuleEvent event ) throws MuleException
                {
                    Object payload= event.getMessage().getPayload();
                    assertEquals( "payload has wrong class", byte[].class, payload.getClass() );
                    assertTrue( "content invalid", Data.validateContent( (byte[]) payload, getContentSize() ) );
                    spyActivated.set( true );
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    @Test(timeout= 20000L)
    public void testInboundRequest() throws URISyntaxException 
    {
        spyRequestMessage();

        for ( Code call : inboundCalls )
        {
            spyActivated.set( false );
            CoapClient client= getClient( getPath( call ) );
            client.useLateNegotiation();
            Request request= new Request( call );
            request.setPayload( Data.getContent( getContentSize() ) );

            CoapResponse response= client.advanced( request );

            validateInboundResponse( call, response );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testInboundRequestEarlyNegotiation() throws URISyntaxException 
    {
        spyRequestMessage();

        for ( Code call : inboundCalls )
        {
            spyActivated.set( false );
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );
            Request request= new Request( call );
            request.setPayload( Data.getContent( getContentSize() ) );

            CoapResponse response= client.advanced( request );

            validateInboundResponse( call, response );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testOutboundRequest() throws URISyntaxException
    {
        mockResponseMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            validateOutboundResponse( call, response );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testOutboundRequestEarlyNegotiation() throws URISyntaxException
    {
        mockResponseMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );

            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            validateOutboundResponse( call, response );

            client.shutdown();
        }
    }
}