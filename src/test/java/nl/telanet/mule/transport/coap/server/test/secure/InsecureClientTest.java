package nl.telanet.mule.transport.coap.server.test.secure;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class InsecureClientTest extends FunctionalMunitSuite
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
    }

    @Before
    public void setUp() throws Exception
    {
        uri= new URI( "coap", null, "127.0.0.1", 5684, null, null, null );
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
        client.setTimeout( 1000L );
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
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    @Test()
    public void testInsecureClientGet() throws Exception
    {
        spyRequestMessage();

        Code call= Code.GET;
        spyActivated= false;
        CoapClient client= getClient( getPath( call ) );
        Request request= new Request( call );
        request.setPayload( "nothing important" );

        CoapResponse response= client.advanced( request );

        assertNull( "should not receive a response", response );
        assertFalse( "spy was unecpectedly activated", spyActivated );

        client.shutdown();

    }

    @Test()
    public void testInsecureClientPost() throws Exception
    {
        spyRequestMessage();

        Code call= Code.POST;
        spyActivated= false;
        CoapClient client= getClient( getPath( call ) );
        Request request= new Request( call );
        request.setPayload( "nothing important" );

        CoapResponse response= client.advanced( request );

        assertNull( "should not receive a response", response );
        assertFalse( "spy was unecpectedly activated", spyActivated );

        client.shutdown();

    }

    @Test()
    public void testInsecureClientPut() throws Exception
    {
        spyRequestMessage();

        Code call= Code.PUT;
        spyActivated= false;
        CoapClient client= getClient( getPath( call ) );
        Request request= new Request( call );
        request.setPayload( "nothing important" );

        CoapResponse response= client.advanced( request );

        assertNull( "should not receive a response", response );
        assertFalse( "spy was unecpectedly activated", spyActivated );

        client.shutdown();

    }

    @Test()
    public void testInsecureClientDelete() throws Exception
    {
        spyRequestMessage();

        Code call= Code.DELETE;
        spyActivated= false;
        CoapClient client= getClient( getPath( call ) );
        Request request= new Request( call );
        request.setPayload( "nothing important" );

        CoapResponse response= client.advanced( request );

        assertNull( "should not receive a response", response );
        assertFalse( "spy was unecpectedly activated", spyActivated );

        client.shutdown();

    }

}