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


public class ResourceExistsTest extends FunctionalMunitSuite
{
    private URI uri= null;

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
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test(timeout= 2000L)
    public void testResourceExists() throws Exception
    {
        CoapClient client= getClient( "/service/exists" );
        Request request= new Request( Code.GET );
        request.getOptions().addUriQuery( "/service/resource-to-remove" );
        CoapResponse response= client.advanced( request );
        assertNotNull( "get exists gave no response", response );
        assertTrue( "response get exists indicates failure", response.isSuccess() );
        assertEquals( "get exists gave wrong response", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "get exists gave wrong payload", Boolean.TRUE.toString(), response.getResponseText() );

        client= getClient( "/service/resource-to-remove" );
        response= client.delete();
        assertNotNull( "got no response on delete", response );
        assertEquals( "wrong response on delete", ResponseCode.DELETED, response.getCode() );

        client= getClient( "/service/exists" );
        request= new Request( Code.GET );
        request.getOptions().addUriQuery( "/service/resource-to-remove" );
        response= client.advanced( request );
        assertNotNull( "get exists gave no response", response );
        assertTrue( "response get exists indicates failure", response.isSuccess() );
        assertEquals( "get exists gave wrong response", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "get exists gave wrong payload", Boolean.FALSE.toString(), response.getResponseText() );

    }

    @Test(timeout= 2000L)
    public void testAddedResourceExists() throws Exception
    {
        CoapClient client= getClient( "/service/exists" );
        Request request= new Request( Code.GET );
        request.getOptions().addUriQuery( "/service/temporary-resource" );
        CoapResponse response= client.advanced( request );
        assertNotNull( "get exists gave no response", response );
        assertTrue( "response get exists indicates failure", response.isSuccess() );
        assertEquals( "get exists gave wrong response", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "get exists gave wrong payload", Boolean.FALSE.toString(), response.getResponseText() );

        client= getClient( "/service/add_resource/all_methods" );
        request= new Request( Code.POST );
        request.setPayload( "some content" );
        request.getOptions().addLocationPath( "service" ).addLocationPath( "temporary-resource" );
        response= client.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        client= getClient( "/service/exists" );
        request= new Request( Code.GET );
        request.getOptions().addUriQuery( "/service/temporary-resource" );
        response= client.advanced( request );
        assertNotNull( "get exists gave no response", response );
        assertTrue( "response get exists indicates failure", response.isSuccess() );
        assertEquals( "get exists gave wrong response", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "get exists gave wrong payload", Boolean.TRUE.toString(), response.getResponseText() );

        client= getClient( "/service/temporary-resource" );
        response= client.delete();
        assertNotNull( "got no response on delete", response );
        assertEquals( "wrong response on delete", ResponseCode.DELETED, response.getCode() );

        client= getClient( "/service/exists" );
        request= new Request( Code.GET );
        request.getOptions().addUriQuery( "/service/temporary-resource" );
        response= client.advanced( request );
        assertNotNull( "get exists gave no response", response );
        assertTrue( "response get exists indicates failure", response.isSuccess() );
        assertEquals( "get exists gave wrong response", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "get exists gave wrong payload", Boolean.FALSE.toString(), response.getResponseText() );
    }


}
