package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class RequestRelationTest extends FunctionalMunitSuite
{
    URI uri= null;

    CoapClient client= null;
    String expected= "";

    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver-RequestRelation.xml";
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
        uri= new URI( "coap", "127.0.0.1", null, null );
    }

    @After
    public void tearDown() throws Exception
    {
        if ( client != null ) client.shutdown();
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( "/requesturi" + path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test
    public void testGet()
    {
        String path= "/get_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testPut()
    {
        String path= "/put_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.put( "nothing important", 0 );

        assertNotNull( "put gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testPost()
    {
        String path= "/post_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.post( "nothing important", 0 );

        assertNotNull( "post gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testDelete()
    {
        String path= "/delete_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.delete();

        assertNotNull( "delete gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", expected, response.getResponseText() );
    }
}