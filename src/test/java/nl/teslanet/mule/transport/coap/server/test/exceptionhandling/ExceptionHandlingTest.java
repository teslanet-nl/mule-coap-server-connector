package nl.teslanet.mule.transport.coap.server.test.exceptionhandling;


import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class ExceptionHandlingTest extends FunctionalMunitSuite
{
    private URI uri= null;

    private static ArrayList< Code > calls;

    private static HashMap< Code, ResponseCode > responses;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/exceptionhandling/testserver1.xml";
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
        calls= new ArrayList< Code >();
        calls.add( Code.GET );
        calls.add( Code.PUT );
        calls.add( Code.POST );
        calls.add( Code.DELETE );

        responses= new HashMap< Code, ResponseCode >();
        responses.put( Code.GET, ResponseCode.CONTENT );
        responses.put( Code.PUT, ResponseCode.CREATED );
        responses.put( Code.POST, ResponseCode.CHANGED );
        responses.put( Code.DELETE, ResponseCode.DELETED );
    }

    @Before
    public void setUp() throws Exception
    {
        uri= new URI( "coap", "127.0.0.1", null, null );
    }

    @After
    public void tearDown() throws Exception
    {
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test(timeout= 2000L)
    public void testHandledException() throws Exception
    {
        CoapClient client= getClient( "/service/handled_exception" );
        for ( Code call : calls )
        {
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response on: " + call.toString(), response );
            assertTrue( "response indicates failure on: " + call.toString(), response.isSuccess() );
            assertEquals( "wrong responsecode on: " + call.toString(), responses.get( call ), response.getCode() );
            assertEquals( "wrong response payload on: " + call.toString(), "exception catched", response.getResponseText() );
        }
        client.shutdown();
    }

    @Test(timeout= 2000L)
    public void testUnhandledException() throws Exception
    {
        CoapClient client= getClient( "/service/unhandled_exception" );
        for ( Code call : calls )
        {
            Request request= new Request( call );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response on: " + call.toString(), response );
            assertFalse( "response does not indicates failure on: " + call.toString(), response.isSuccess() );
            assertEquals( "wrong responsecode on: " + call.toString(), ResponseCode.INTERNAL_SERVER_ERROR, response.getCode() );
            assertEquals( "wrong response payload on: " + call.toString(), "EXCEPTION IN PROCESSING FLOW", response.getResponseText() );
        }
        client.shutdown();
    }

}