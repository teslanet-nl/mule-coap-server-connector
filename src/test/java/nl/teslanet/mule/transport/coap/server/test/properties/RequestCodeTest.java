package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.*;

import java.net.URI;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;


public class RequestCodeTest extends FunctionalTestCase
{
    URI uri= null;

    CoapClient client= null;

    @Override
    protected String getConfigFile()
    {
        return "mule-config/properties/testserver-RequestCode.xml";
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

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test
    public void testGetRequestCode()
    {
            String path= "/requestcode/get_me";
            String payload= "GET";

            CoapClient client= new CoapClient( uri.resolve( path ) );
            client.setTimeout( 1000L );

            CoapResponse response= client.get();

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure", response.isSuccess() );
            assertEquals( "echoed request code has wrong value", payload, response.getResponseText() );
    }
    
    @Test
    public void testPutRequestCode()
    {
        String path= "/requestcode/put_me";
        String payload= "PUT";

        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.put("nothing important", 0);

        assertNotNull( "put gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", payload, response.getResponseText() );
    }
     
    @Test
    public void testPostRequestCode()
    {
        String path= "/requestcode/post_me";
        String payload= "POST";

        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.post("nothing important", 0);

        assertNotNull( "post gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", payload, response.getResponseText() );
     }
    @Test
    public void testDeleteRequestCode()
    {
        String path= "/requestcode/delete_me";
        String payload= "DELETE";

        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );

        CoapResponse response= client.delete();

        assertNotNull( "delete gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request code has wrong value", payload, response.getResponseText() );
     }
}