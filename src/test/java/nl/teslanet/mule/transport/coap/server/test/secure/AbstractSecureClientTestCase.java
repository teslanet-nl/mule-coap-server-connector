package nl.teslanet.mule.transport.coap.server.test.secure;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import nl.teslanet.mule.transport.coap.server.test.utils.Data;

//TODO review
public abstract class AbstractSecureClientTestCase extends FunctionalMunitSuite
{
    private static URI uri= null;

    private CoapClient client= null;

    private boolean spyActivated;

    private static ArrayList< Code > inboundCalls;

    private static ArrayList< Code > outboundCalls;

    private static HashMap< Code, String > paths;

    private static CoapEndpoint endpoint;

    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver1.xml";
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

        uri= new URI( "coaps", "127.0.0.1", null, null, null );

        //keyStore
        KeyStore keyStore= KeyStore.getInstance( "JKS" );
        InputStream in= Data.readResourceAsStream( "certs/keyStore.jks" );
        keyStore.load( in, "endPass".toCharArray() );
        //in.close();

        //trustStore
        KeyStore trustStore= KeyStore.getInstance( "JKS" );
        in= Data.readResourceAsStream( "certs/trustStore.jks" );
        trustStore.load( in, "rootPass".toCharArray() );
        //in.close();

        // You can load multiple certificates if needed
        Certificate[] trustedCertificates= new Certificate [1];
        trustedCertificates[0]= trustStore.getCertificate( "root" );

        //pskStore
        InMemoryPskStore pskStore= new InMemoryPskStore();

        //dtls builder
        DtlsConnectorConfig.Builder dtlsBuilder= new DtlsConnectorConfig.Builder( new InetSocketAddress( 0 ));

        dtlsBuilder.setPskStore( pskStore );
        dtlsBuilder.setIdentity( (PrivateKey) keyStore.getKey( "client", "endPass".toCharArray() ), keyStore.getCertificateChain( "client" ), true );
        dtlsBuilder.setTrustStore( trustedCertificates );
//        dtlsBuilder.setEnableAddressReuse( false );
//        dtlsBuilder.setConnectionThreadCount( 1 );

        //connector

        DTLSConnector dtlsConnector= new DTLSConnector( dtlsBuilder.build() );

        //endpoint

        NetworkConfig config= NetworkConfig.createStandardWithoutFile();
        config.setInt( NetworkConfig.Keys.ACK_TIMEOUT, 20000 );
        config.setLong( NetworkConfig.Keys.EXCHANGE_LIFETIME, 30000L );
//        config.setLong(NetworkConfig.Keys.DTLS_AUTO_RESUME_TIMEOUT, 30000L );
        endpoint= new CoapEndpoint( dtlsConnector, config );
        endpoint.start();
    }

    @AfterClass
    static public void tearDownClass()
    {
        endpoint.stop();
        endpoint.destroy();
    }

    @Before
    public void setUp() throws Exception
    {
        client= new CoapClient();
        client.setTimeout( 200000L );
        client.setEndpoint( endpoint );
    }

    @After
    public void tearDown() throws Exception
    {
        if ( client != null ) client.shutdown();
        client= null;
    }

    protected void expectException()
    {
        // Default None      
    }

    protected String getPath( Code call )
    {
        return paths.get( call );
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
                    assertArrayEquals( "content invalid", "test-payload".getBytes(), (byte[]) payload );
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

    @Test(timeout= 20000L)
    public void testInboundRequest() throws Exception
    {
        spyRequestMessage();
        expectException();

        for ( Code call : inboundCalls )
        {
            spyActivated= false;
            client.useLateNegotiation();
            client.setURI( uri.resolve( getPath( call ) ).toString() );
            Request request= new Request( call );
            switch ( call )
            {
                case DELETE:
                case GET:
//                    request.setUnintendedPayload();
                    break;
                default:
                    break;
            }
            request.setPayload( "test-payload" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertTrue( "spy was not activated", spyActivated );

            client.shutdown();
        }
    }

    @Test
    public void testOutboundRequest() throws Exception
    {
        mockResponseMessage();
        expectException();

        for ( Code call : outboundCalls )
        {
            client.setURI( uri.resolve( getPath( call ) ).toString() );
            Request request= new Request( call );
            switch ( call )
            {
                case DELETE:
                case GET:
//                    request.setUnintendedPayload();
                    break;
                default:
                    break;
            }
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure: " + response.getCode() + " msg: " + response.getResponseText(), response.isSuccess() );
            assertArrayEquals( "wrong payload in response", "test-payload".getBytes(), response.getPayload() );

            client.shutdown();
        }
    }

}