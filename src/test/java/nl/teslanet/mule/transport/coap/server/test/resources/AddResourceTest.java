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


public class AddResourceTest extends FunctionalMunitSuite
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
    public void testAddResource() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.CONTENT );
        responses.put( Code.PUT, ResponseCode.CHANGED );
        responses.put( Code.POST, ResponseCode.CHANGED );
        responses.put( Code.DELETE, ResponseCode.DELETED );

        CoapClient client= getClient( "/service/resource1" );
        CoapResponse response= client.get( );
        assertNotNull( "get resoure1 gave no response", response );
        assertTrue( "response get resoure1 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );

        client= getClient( "/service/resource2" );
        response= client.get( );
        assertNotNull( "get resoure2 gave no response", response );
        //assertFalse( "response first get resoure2 should not indicate succes", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.NOT_FOUND, response.getCode() );

        client= getClient( "/service/add_resource/all_methods" );
        Request request= new Request( Code.POST );
        request.setPayload( "some content" );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "resource2" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        for ( Code call : calls )
        {
            client= getClient( "/service/resource2" );
            request= new Request( call );
            request.setPayload( "some other content" );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertTrue( "response indicates failure on: " + call, response.isSuccess() );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }
    
    @Test(timeout= 2000L)
    public void testAddResourceGetOnly() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.CONTENT );
        responses.put( Code.PUT, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.POST, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.DELETE, ResponseCode.METHOD_NOT_ALLOWED );

        CoapClient client= getClient( "/service/resource1" );
        CoapResponse response= client.get( );
        assertNotNull( "get resoure1 gave no response", response );
        assertTrue( "response get resoure1 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );

        client= getClient( "/service/get-resource" );
        response= client.get( );
        assertNotNull( "get resoure2 gave no response", response );
        //assertFalse( "response first get resoure2 should not indicate succes", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.NOT_FOUND, response.getCode() );

        client= getClient( "/service/add_resource/get_only" );
        Request request= new Request( Code.POST );
        request.setPayload( "some content" );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "get-resource" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        for ( Code call : calls )
        {
            client= getClient( "/service/get-resource" );
            request= new Request( call );
            request.setPayload( "some other content" );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }

    @Test(timeout= 2000L)
    public void testAddResourcePostOnly() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.POST, ResponseCode.CHANGED );
        responses.put( Code.PUT, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.DELETE, ResponseCode.METHOD_NOT_ALLOWED );

        CoapClient client= getClient( "/service/resource1" );
        CoapResponse response= client.get( );
        assertNotNull( "get resoure1 gave no response", response );
        assertTrue( "response get resoure1 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );

        client= getClient( "/service/post-resource" );
        response= client.get( );
        assertNotNull( "get resoure2 gave no response", response );
        //assertFalse( "response first get resoure2 should not indicate succes", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.NOT_FOUND, response.getCode() );

        client= getClient( "/service/add_resource/post_only" );
        Request request= new Request( Code.POST );
        request.setPayload( "some content" );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "post-resource" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        for ( Code call : calls )
        {
            client= getClient( "/service/post-resource" );
            request= new Request( call );
            request.setPayload( "some other content" );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }

    @Test(timeout= 2000L)
    public void testAddResourcePutOnly() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.POST, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.PUT, ResponseCode.CHANGED );
        responses.put( Code.DELETE, ResponseCode.METHOD_NOT_ALLOWED );

        CoapClient client= getClient( "/service/resource1" );
        CoapResponse response= client.get( );
        assertNotNull( "get resoure1 gave no response", response );
        assertTrue( "response get resoure1 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );

        client= getClient( "/service/put-resource" );
        response= client.get( );
        assertNotNull( "get resoure2 gave no response", response );
        //assertFalse( "response first get resoure2 should not indicate succes", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.NOT_FOUND, response.getCode() );

        client= getClient( "/service/add_resource/put_only" );
        Request request= new Request( Code.POST );
        request.setPayload( "some content" );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "put-resource" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        for ( Code call : calls )
        {
            client= getClient( "/service/put-resource" );
            request= new Request( call );
            request.setPayload( "some other content" );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }
    
    @Test(timeout= 2000L)
    public void testAddResourceDeleteOnly() throws Exception
    {
        responses.clear();
        responses.put( Code.GET, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.POST, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.PUT, ResponseCode.METHOD_NOT_ALLOWED );
        responses.put( Code.DELETE, ResponseCode.DELETED );

        CoapClient client= getClient( "/service/resource1" );
        CoapResponse response= client.get( );
        assertNotNull( "get resoure1 gave no response", response );
        assertTrue( "response get resoure1 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );

        client= getClient( "/service/delete-resource" );
        response= client.get( );
        assertNotNull( "get resoure2 gave no response", response );
        //assertFalse( "response first get resoure2 should not indicate succes", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.NOT_FOUND, response.getCode() );

        client= getClient( "/service/add_resource/delete_only" );
        Request request= new Request( Code.POST );
        request.setPayload( "some content" );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "delete-resource" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        for ( Code call : calls )
        {
            client= getClient( "/service/delete-resource" );
            request= new Request( call );
            request.setPayload( "some other content" );
            response= client.advanced( request );
            assertNotNull( "got no response on: " + call, response );
            assertEquals( "wrong response on: " + call, responses.get( call ), response.getCode() );
        }
    }
}
