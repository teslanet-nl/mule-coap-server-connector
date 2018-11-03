package nl.teslanet.mule.transport.coap.server.test.basic;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;


public class GetMethodTest extends FunctionalTestCase
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
        String path= "/basic/get_me";

        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be CONTENT", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "response payload has wrong value", path, response.getResponseText() );
    }

    @Test
    public void testNoGetAllowed()
    {
        String path= "/basic/do_not_get_me";
        
        client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be METHOD_NOT_ALLOWED", ResponseCode.METHOD_NOT_ALLOWED, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
    }

    @Test
    public void testSuccess2()
    {
        String path= "/basic/get_me2";

        client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be CONTENT", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "response payload has wrong value", path, response.getResponseText() );
    }

    @Test
    public void testNoResource()
    {
        String path= "/basic/do_not_get_me3";

        client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be NOT_FOUND", ResponseCode.NOT_FOUND, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
    }
}