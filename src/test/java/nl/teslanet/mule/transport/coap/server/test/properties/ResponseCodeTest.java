package nl.teslanet.mule.transport.coap.server.test.properties;


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


public class ResponseCodeTest extends FunctionalTestCase
{
    URI uri= null;

    CoapClient client= null;

    @Override
    protected String getConfigFile()
    {
        return "mule-config/properties/testserver-ResponseCode.xml";
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
    public void testGet()
    {
        CoapResponse response;

        for ( ResponseCode code : ResponseCode.values() )
        {
            if ( !code.name().startsWith( "_" ))
            {
                client= getClient( "/responsecode/always_" + code.name() );
                response= client.get();
                assertNotNull( "get gave no response", response );
                assertEquals( "get didn't return response code: " + code.name(), code, response.getCode() );
            }
        }
    }
    
    @Test
    public void testPut()
    {
        CoapResponse response;

        for ( ResponseCode code : ResponseCode.values() )
        {
            if ( !code.name().startsWith( "_" ))
            {
                client= getClient( "/responsecode/always_" + code.name() );
                response= client.put("put-payload", 0);
                assertNotNull( "put gave no response", response );
                assertEquals( "put didn't return response code: " + code.name(), code, response.getCode() );
            }
        }
    }
     
    @Test
    public void testPost()
    {
        CoapResponse response;

        for ( ResponseCode code : ResponseCode.values() )
        {
            if ( !code.name().startsWith( "_" ))
            {
                client= getClient( "/responsecode/always_" + code.name() );
                response= client.post("post-payload", 0);
                assertNotNull( "post gave no response", response );
                assertEquals( "post didn't return response code: " + code.name(), code, response.getCode() );
            }
        }
    }
    @Test
    public void testDelete()
    {
        CoapResponse response;

        for ( ResponseCode code : ResponseCode.values() )
        {
            if ( !code.name().startsWith( "_" ))
            {
                client= getClient( "/responsecode/always_" + code.name() );
                response= client.delete();
                assertNotNull( "delete gave no response", response );
                assertEquals( "delete didn't return response code: " + code.name(), code, response.getCode() );
            }
        }
    }
}