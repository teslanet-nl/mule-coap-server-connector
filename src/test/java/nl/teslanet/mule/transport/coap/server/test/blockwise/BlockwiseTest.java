package nl.teslanet.mule.transport.coap.server.test.blockwise;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint.CoapEndpointBuilder;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import nl.teslanet.mule.transport.coap.server.test.utils.Data;


public class BlockwiseTest extends FunctionalMunitSuite
{
    /**
     * Size of large testcontent
     */
    private final int SMALL_CONTENT_SIZE= 10;
    private final int LARGE_CONTENT_SIZE= 8192;
    private final int EXTRA_LARGE_CONTENT_SIZE= 16000;

    URI uri= null;

    CoapClient client= null;

    private boolean spyActivated;

    private static ArrayList< Code > inboundCalls;

    private static ArrayList< Code > outboundCalls;

    private static HashMap< Code, String > paths;
    
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

    //TODO look into difference with non-blockwise 
    @BeforeClass
    static public void setUpClass() throws Exception
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

        paths= new HashMap< Code, String >();
        paths.put( Code.GET, "/service/get_me" );
        paths.put( Code.PUT, "/service/put_me" );
        paths.put( Code.POST, "/service/post_me" );
        paths.put( Code.DELETE, "/service/delete_me" );
    }

    @Before
    public void setUp() throws Exception
    {
        uri= new URI( "coap", "127.0.0.1", null, null );
    }

    @After
    public void tearDown() throws Exception
    {
        if ( client != null ) client.shutdown();
    }

    protected String getPath( Code call )
    {
        return paths.get( call );
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 2000L );
        NetworkConfig config= NetworkConfig.createStandardWithoutFile();
        config.setInt( NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE, EXTRA_LARGE_CONTENT_SIZE );
        CoapEndpointBuilder endpointBuilder= new CoapEndpointBuilder();
        endpointBuilder.setNetworkConfig( config );
        client.setEndpoint( endpointBuilder.build());
        return client;
    }

    private void spyRequestExtraLargeMessage()
    {
        SpyProcess beforeSpy= new SpyProcess()
            {
                @Override
                public void spy( MuleEvent event ) throws MuleException
                {
                    Object payload= event.getMessage().getPayload();
                    assertEquals( "payload has wrong class", byte[].class, payload.getClass() );
                    assertTrue( "content invalid", Data.validateContent( (byte[]) payload, EXTRA_LARGE_CONTENT_SIZE ) );
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    private void mockResponseExtraLargeMessage()
    {
        MuleMessage messageToBeReturned= muleMessageWithPayload( Data.getContent(EXTRA_LARGE_CONTENT_SIZE) );
        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        mocker.thenReturn( messageToBeReturned );
    }
    
    private void spyRequestLargeMessage()
    {
        SpyProcess beforeSpy= new SpyProcess()
            {
                @Override
                public void spy( MuleEvent event ) throws MuleException
                {
                    Object payload= event.getMessage().getPayload();
                    assertEquals( "payload has wrong class", byte[].class, payload.getClass() );
                    assertTrue( "content invalid", Data.validateContent( (byte[]) payload, LARGE_CONTENT_SIZE ) );
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    
    private void mockResponseLargeMessage()
    {
        MuleMessage messageToBeReturned= muleMessageWithPayload( Data.getContent(LARGE_CONTENT_SIZE) );
        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        mocker.thenReturn( messageToBeReturned );
    }

    private void spyRequestSmallMessage()
    {
        SpyProcess beforeSpy= new SpyProcess()
            {
                @Override
                public void spy( MuleEvent event ) throws MuleException
                {
                    Object payload= event.getMessage().getPayload();
                    assertEquals( "payload has wrong class", byte[].class, payload.getClass() );
                    assertTrue( "content invalid", Data.validateContent( (byte[]) payload, SMALL_CONTENT_SIZE ));
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    private void mockResponseSmallMessage()
    {
        MuleMessage messageToBeReturned= muleMessageWithPayload( Data.getContent( SMALL_CONTENT_SIZE ) );
        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        mocker.thenReturn( messageToBeReturned );
    }
    
    @Test(timeout= 20000L)
    public void testExtraLargeInboundRequest() throws Exception
    {
        spyRequestExtraLargeMessage();

        for ( Code call : inboundCalls )
        {
            //TODO californium seems not to support inbound blockwise for GET
            if ( call == Code.GET ) continue;
            //TODO californium seems not to support inbound blockwise for DELETE
            if ( call == Code.DELETE ) continue;
            
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useLateNegotiation();
            Request request= new Request( call );
            request.setPayload( Data.getContent(EXTRA_LARGE_CONTENT_SIZE) );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertEquals( "response is not REQUEST_ENTITY_TOO_LARGE : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), ResponseCode.REQUEST_ENTITY_TOO_LARGE, response.getCode() );
        }
    }
    
    @Test(timeout= 20000L)
    public void testExtraLargeOutboundRequest() throws Exception
    {
        mockResponseExtraLargeMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "wrong payload in response on: " + call, Data.validateContent( response.getPayload(),EXTRA_LARGE_CONTENT_SIZE ) );

            client.shutdown();
        }
    }
    
    @Test(timeout= 20000L)
    public void testLargeInboundRequest() throws Exception
    {
        spyRequestLargeMessage();

        for ( Code call : inboundCalls )
        {
            //TODO californium seems not to support inbound blockwise for GET
            if ( call == Code.GET ) continue;
            //TODO californium seems not to support inbound blockwise for DELETE
            if ( call == Code.DELETE ) continue;
            
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useLateNegotiation();
            Request request= new Request( call );
            request.setPayload( Data.getContent(LARGE_CONTENT_SIZE) );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated on: " + call, spyActivated );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testLargeInboundRequestEarlyNegotiation() throws Exception
    {
        spyRequestLargeMessage();

        for ( Code call : inboundCalls )
        {
            //TODO californium seems not to support inbound blockwise for GET
            if ( call == Code.GET ) continue;
            //TODO californium seems not to support inbound blockwise for DELETE
            if ( call == Code.DELETE ) continue;
            
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );
            Request request= new Request( call );
            request.setPayload( Data.getContent(LARGE_CONTENT_SIZE) );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated on: " + call, spyActivated );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testLargeOutboundRequest() throws Exception
    {
        mockResponseLargeMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "wrong payload in response on: " + call, Data.validateContent( response.getPayload(),LARGE_CONTENT_SIZE ) );

            client.shutdown();
        }
    }
    
    @Test(timeout= 20000L)
    public void testLargeOutboundRequestEarlyNegotiation() throws Exception
    {
        mockResponseLargeMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );

            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "wrong payload in response on: " + call, Data.validateContent( response.getPayload(),LARGE_CONTENT_SIZE ) );

            client.shutdown();
        }
    }

    
    @Test(timeout= 20000L)
    public void testSmallInboundRequest() throws Exception
    {
        spyRequestSmallMessage();

        for ( Code call : inboundCalls )
        {
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useLateNegotiation();
            Request request= new Request( call );
            request.setPayload( Data.getContent( SMALL_CONTENT_SIZE ) );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated on: " + call, spyActivated );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testSmallInboundRequestEarlyNegotiation() throws Exception
    {
        spyRequestSmallMessage();

        for ( Code call : inboundCalls )
        {
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );
            Request request= new Request( call );
            request.setPayload( Data.getContent( SMALL_CONTENT_SIZE ) );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated on: " + call, spyActivated );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testSmallOutboundRequest() throws Exception
    {
        mockResponseSmallMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure on : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "wrong payload in response on: " + call, Data.validateContent( response.getPayload(), SMALL_CONTENT_SIZE) );

            client.shutdown();
        }
    }
    
    @Test(timeout= 20000L)
    public void testSmallOutboundRequestEarlyNegotiation() throws Exception
    {
        mockResponseSmallMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );

            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "no response on: " + call, response );
            assertTrue( "response indicates failure: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "wrong payload in response on: " + call, Data.validateContent( response.getPayload(), SMALL_CONTENT_SIZE) );

            client.shutdown();
        }
    }
}