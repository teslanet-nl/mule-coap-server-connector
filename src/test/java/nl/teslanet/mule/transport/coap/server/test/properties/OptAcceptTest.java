package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class OptAcceptTest extends FunctionalMunitSuite
{
    URI uri= null;

    CoapClient client= null;

    private Class< Integer > expectedClass= Integer.class;
    private Integer expected= 41;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver-OptAccept.xml";
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
        CoapClient client= new CoapClient( uri.resolve( "/opt_accept" + path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test
    public void testGet()
    {
        String path= "/get_me";

        CoapClient client= getClient( path );
        

        CoapResponse response= client.get( expected );

        assertNotNull( "get gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        Object payload= SerializationUtils.deserialize( response.getPayload());
        assertEquals( "option has wrong class", expectedClass, payload.getClass() );
        assertEquals( "option has wrong value", expected, payload );
    }

    @Test
    public void testPut()
    {
        String path= "/put_me";

        CoapClient client= getClient( path );
        Request request= new Request(Code.PUT);
        request.setPayload( "nothing important" ).getOptions().setAccept( expected );
        
        CoapResponse response= client.advanced( request );

        assertNotNull( "put gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        Object payload= SerializationUtils.deserialize( response.getPayload());
        assertEquals( "option has wrong class", expectedClass, payload.getClass() );
        assertEquals( "option has wrong value", expected, payload );
    }

    @Test
    public void testPost()
    {
        String path= "/post_me";

        CoapClient client= getClient( path );
        Request request= new Request(Code.POST);
        request.setPayload( "nothing important" ).getOptions().setAccept( expected );
        
        CoapResponse response= client.advanced( request );

        assertNotNull( "post gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        Object payload= SerializationUtils.deserialize( response.getPayload());
        assertEquals( "option has wrong class", expectedClass, payload.getClass() );
        assertEquals( "option has wrong value", expected, payload );
    }

    @Test
    public void testDelete()
    {
        String path= "/delete_me";

        CoapClient client= getClient( path );
        Request request= new Request(Code.DELETE);
        request.setPayload( "nothing important" ).getOptions().setAccept( expected );
        
        CoapResponse response= client.advanced( request );

        assertNotNull( "delete gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        Object payload= SerializationUtils.deserialize( response.getPayload());
        assertEquals( "option has wrong class", expectedClass, payload.getClass() );
        assertEquals( "option has wrong value", expected, payload );
    }
}