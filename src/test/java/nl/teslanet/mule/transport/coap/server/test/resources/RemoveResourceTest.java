package nl.teslanet.mule.transport.coap.server.test.resources;


import static org.junit.Assert.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Request;
import org.junit.Before;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class RemoveResourceTest extends FunctionalMunitSuite
{
    private URI uri= null;

    private static ArrayList< Code > calls;

    private static HashMap< Code, ResponseCode > responses;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/resources/testserver1.xml";
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
        calls= new ArrayList< Code >();
        calls.add( Code.GET );
        calls.add( Code.PUT );
        calls.add( Code.POST );
        calls.add( Code.DELETE );

        responses= new HashMap< Code, ResponseCode >();
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test(timeout= 2000L)
    public void testRemoveResource() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.NOT_FOUND );
        responses.put( Code.POST, ResponseCode.NOT_FOUND );
        responses.put( Code.PUT, ResponseCode.NOT_FOUND );
        responses.put( Code.DELETE, ResponseCode.NOT_FOUND );

        CoapClient client= getClient( "/service/resource-to-remove" );
        CoapResponse response= client.put( "some content", 0 );
        assertNotNull( "put resource gave no response", response );
        assertEquals( "put gave wrong response", ResponseCode.CHANGED, response.getCode() );

        client= getClient( "/service/resource-to-remove" );
        response= client.delete();
        assertNotNull( "got no response on delete", response );
        assertEquals( "wrong response on delete", ResponseCode.DELETED, response.getCode() );

        for ( Code call : calls )
        {
            Request request= new Request( call );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }

    @Test(timeout= 2000L)
    public void testRemoveAddedResource() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.NOT_FOUND );
        responses.put( Code.POST, ResponseCode.NOT_FOUND );
        responses.put( Code.PUT, ResponseCode.NOT_FOUND );
        responses.put( Code.DELETE, ResponseCode.NOT_FOUND );

        CoapClient client= getClient( "/service/resource1" );
        CoapResponse response= client.get();
        assertNotNull( "get resoure1 gave no response", response );
        assertTrue( "response get resoure1 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );

        client= getClient( "/service/resource-to-remove2" );
        response= client.get();
        assertNotNull( "get resoure2 gave no response", response );
        assertEquals( "get gave wrong response", ResponseCode.NOT_FOUND, response.getCode() );

        client= getClient( "/service/add_resource/all_methods" );
        Request request= new Request( Code.POST );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "resource-to-remove2" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        client= getClient( "/service/resource-to-remove2" );
        response= client.delete();
        assertNotNull( "got no response on delete", response );
        assertEquals( "wrong response on delete", ResponseCode.DELETED, response.getCode() );

        for ( Code call : calls )
        {
            client= getClient( "/service/resource-to-remove2" );
            request= new Request( call );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }

}
