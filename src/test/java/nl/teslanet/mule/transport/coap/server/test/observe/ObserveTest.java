package nl.teslanet.mule.transport.coap.server.test.observe;


import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.junit.Before;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class ObserveTest extends FunctionalMunitSuite
{
    private URI uri= null;

    private AtomicInteger handlerErrors= new AtomicInteger();

    private CopyOnWriteArrayList< CoapResponse > observations= new CopyOnWriteArrayList< CoapResponse >();

    private ArrayList< String > contents= new ArrayList< String >();

    @Override
    protected String getConfigResources()
    {
        return "mule-config/observe/testserver1.xml";
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
        handlerErrors.set( 0 );
        observations.clear();
        contents.clear();
        contents.add( "first" );
        contents.add( "second" );
        contents.add( "third" );
        contents.add( "fourth" );
        contents.add( "fifth" );
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 2000L );
        return client;
    }

    private CoapHandler getHandler()
    {
        CoapHandler handler= new CoapHandler()
            {
                @Override
                public void onError()
                {
                    handlerErrors.incrementAndGet();
                }

                @Override
                public void onLoad( CoapResponse response )
                {
                    observations.add( response );
                }

            };
        return handler;
    }

    @Test(timeout= 10000L)
    public void testObserve() throws Exception
    {

        CoapClient client= getClient( "/service/observe_me" );
        CoapResponse response= client.put( contents.get( 0 ), 0 );
        assertNotNull( "put nr: 0 gave no response", response );
        assertTrue( "response nr: 0 indicates failure", response.isSuccess() );

        response= client.get();
        assertNotNull( "get gave no response", response );
        assertTrue( "get response indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong content", contents.get( 0 ), response.getResponseText() );

        CoapObserveRelation relation= client.observe( getHandler() );

        for ( int i= 1; i < contents.size(); i++ )
        {
            Thread.sleep( 100 );
            response= client.put( contents.get( i ), 0 );
            assertNotNull( "put nr: " + i + " gave no response", response );
            assertTrue( "response nr: " + i + " indicates failure", response.isSuccess() );
        }

        Thread.sleep( 100 );
        assertEquals( "handler errors count ", 0, handlerErrors.get() );
        assertEquals( "wrong count of observations", contents.size(), observations.size() );

        for ( int i= 0; i < contents.size(); i++ )
        {
            response= observations.get( i );
            assertNotNull( "observation nr: " + i + " is empty", response );
            assertTrue( "observation nr: " + i + " indicates failure", response.isSuccess() );
            assertEquals( "observation nr: " + i + " has wrong content", contents.get( i ), response.getResponseText() );
        }

        relation.reactiveCancel();
        client.shutdown();
    }
    
    @Test(timeout= 10000000L)
    public void testObserveOnAddedResource() throws Exception
    {

        CoapClient client= getClient( "/service/observe_me_too" );
        CoapResponse response= client.put( contents.get( 0 ), 0 );
        assertNotNull( "put nr: 0 gave no response", response );
        assertFalse( "response nr: 0 indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong content", ResponseCode.NOT_FOUND, response.getCode() );

        CoapClient client2= getClient( "/service" );
        Request request= new Request( Code.POST );
        request.setPayload( contents.get( 0 ));
        request.getOptions().addLocationPath( "service" ).addLocationPath( "observe_me_too" );
        response= client2.advanced( request );
        assertNotNull( "post gave no response", response );
        assertTrue( "post response indicates failure", response.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response.getCode() );

        response= client.get();
        assertNotNull( "get gave no response", response );
        assertTrue( "get response indicates failure", response.isSuccess() );
        assertEquals( "get gave wrong response", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "get gave wrong content", contents.get( 0 ), response.getResponseText() );

        CoapObserveRelation relation= client.observe( getHandler() );

        for ( int i= 1; i < contents.size(); i++ )
        {
            Thread.sleep( 100 );
            response= client.put( contents.get( i ), 0 );
            assertNotNull( "put nr: " + i + " gave no response", response );
            assertTrue( "response nr: " + i + " indicates failure", response.isSuccess() );
        }

        Thread.sleep( 100 );
        assertEquals( "handler errors count ", 0, handlerErrors.get() );
        assertEquals( "wrong count of observations", contents.size(), observations.size() );

        for ( int i= 0; i < contents.size(); i++ )
        {
            response= observations.get( i );
            assertNotNull( "observation nr: " + i + " is empty", response );
            assertTrue( "observation nr: " + i + " indicates failure", response.isSuccess() );
            assertEquals( "observation nr: " + i + " has wrong content", contents.get( i ), response.getResponseText() );
        }

        relation.reactiveCancel();
        client.shutdown();
        client2.shutdown();
    }
}
