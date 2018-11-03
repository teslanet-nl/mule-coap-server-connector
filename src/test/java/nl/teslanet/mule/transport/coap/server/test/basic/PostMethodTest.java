package nl.teslanet.mule.transport.coap.server.test.basic;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;


public class PostMethodTest extends FunctionalTestCase
{
    URI uri= null;

    CoapClient client= null;

    @Override
    protected String getConfigFile()
    {
        return "mule-config/basic/testserver1.xml";
    };

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

    @Test
    public void testSuccess()
    {
        String path= "/basic/post_me";
        String payload= ":payload_post_me";

        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.post(payload, 0);

        assertNotNull( "post gave no response", response );
        assertEquals( "response code should be CHANGED", ResponseCode.CHANGED, response.getCode() );
        assertEquals( "response payload has wrong value", path + payload, response.getResponseText() );
    }

    @Test
    public void testNoPostAllowed()
    {
        String path= "/basic/do_not_post_me";
        String payload= ":payload_do_not_post_me";
        
        client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.post(payload, 0);

        assertNotNull( "post gave no response", response );
        assertEquals( "response code should be METHOD_NOT_ALLOWED", ResponseCode.METHOD_NOT_ALLOWED, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
    }

    @Test
    public void testNoPostAllowedDefault()
    {
        String path= "/basic/do_not_post_me2";
        String payload= ":payload_do_not_post_me2";

        client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.post(payload, 0);

        assertNotNull( "post gave no response", response );
        assertEquals( "response code should be METHOD_NOT_ALLOWED", ResponseCode.METHOD_NOT_ALLOWED, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
    }

    @Test
    public void testNoResource()
    {
        String path= "/basic/do_not_post_me3";
        String payload= ":payload_do_not_post_me3";

        client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.post(payload, 0);

        assertNotNull( "post gave no response", response );
        assertEquals( "response code should be NOT_FOUND", ResponseCode.NOT_FOUND, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
    }
}