package nl.telanet.mule.transport.coap.server.test.secure;


import static org.junit.Assert.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.CoapEndpoint.CoapEndpointBuilder;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig.Builder;
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


public class SecureClientTest extends FunctionalMunitSuite
{
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
        uri= new URI( "coaps", "127.0.0.1", null, null, null );
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
        Builder dtlsBuilder = new DtlsConnectorConfig.Builder();
        dtlsBuilder.setPskStore( Data.readResourceAsString( filePath ));
        DtlsConnectorConfig dtlsConfig = new DtlsConnectorConfig.Builder();
        CoapEndpointBuilder endpointBuilder= new CoapEndpoint.CoapEndpointBuilder();
        NetworkConfig config= new NetworkConfig();
        endpointBuilder.setNetworkConfig( config );
        endpointBuilder.setConnector( new  )
        client.setEndpoint( endpointBuilder.build() );
        return client;
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
                    assertArrayEquals( "content invalid", new String( "test-payload").getBytes(), (byte[])payload);
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    private void mockResponseMessage()
    {
        MuleMessage messageToBeReturned= muleMessageWithPayload( "test-payload" );
        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        mocker.thenReturn( messageToBeReturned );
    }

    @Test
    public void testInboundRequest() throws Exception
    {
        spyRequestMessage();

        for ( Code call : inboundCalls )
        {
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useLateNegotiation();
            Request request= new Request( call );
            request.setPayload( "test-payload" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated", spyActivated );

            client.shutdown();
        }
    }

    @Test(timeout= 20000L)
    public void testLargeInboundRequestEarlyNegotiation() throws Exception
    {
        spyRequestMessage();

        for ( Code call : inboundCalls )
        {
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            client.useEarlyNegotiation( 32 );
            Request request= new Request( call );
            request.setPayload( LargeContent.get() );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated", spyActivated );

            client.shutdown();
        }
    }

    @Test(timeout= 2000000L)
    public void testLargeOutboundRequest() throws Exception
    {
        mockResponseMessage();

        for ( Code call : outboundCalls )
        {
            CoapClient client= getClient( getPath( call ) );
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "wrong payload in response", LargeContent.validate( response.getPayload() ) );

            client.shutdown();
        }
    }

}