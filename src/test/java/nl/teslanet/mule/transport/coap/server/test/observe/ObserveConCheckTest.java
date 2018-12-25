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
import org.junit.Ignore;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class ObserveConCheckTest extends FunctionalMunitSuite
{
    private URI uri= null;

    private AtomicInteger handlerErrors= new AtomicInteger();

    private CopyOnWriteArrayList< ReceivedNotification > observations= new CopyOnWriteArrayList< ReceivedNotification >();

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
        for ( int i= 0; i < 16; ++i )
        {
            contents.add( Integer.toString( i ) );
        }
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
                    observations.add( new ReceivedNotification( response ) );
                }

            };
        return handler;
    }

    @Test(timeout= 20000L)
    public void testConCheckOnCount() throws Exception
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

        for ( int i= 0; i < observations.size(); i++ )
        {
            response= observations.get( i ).response;
            assertNotNull( "observation nr: " + i + " is empty", response );
            assertTrue( "observation nr: " + i + " indicates failure", response.isSuccess() );
            assertEquals( "observation nr: " + i + " has wrong content", contents.get( i ), response.getResponseText() );
            assertEquals( "observation nr: " + i + " is unexpected confirmable", i != 0 && ( i + 1 ) % 5 == 0, response.advanced().isConfirmable() );

        }
        relation.reactiveCancel();
    }

    @Test(timeout= 20000L)
    public void testConCheckOnTime() throws Exception
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
            Thread.sleep( 350 );
            response= client.put( contents.get( i ), 0 );
            assertNotNull( "put nr: " + i + " gave no response", response );
            assertTrue( "response nr: " + i + " indicates failure", response.isSuccess() );
        }

        Thread.sleep( 100 );
        assertEquals( "handler errors count ", 0, handlerErrors.get() );
        assertEquals( "wrong count of observations", contents.size(), observations.size() );
        long ts1= observations.get( 0 ).ts;
        long ts2= 0;
        for ( int i= 0; i < observations.size(); i++ )
        {
            response= observations.get( i ).response;
            ts2= observations.get( i ).ts ;
            boolean expectConfirmable= ( i != 0 && ( ts2 - ts1 ) >= 650 );
            if ( expectConfirmable ) ts1= ts2;
            assertNotNull( "observation nr: " + i + " is empty", response );
            assertTrue( "observation nr: " + i + " indicates failure", response.isSuccess() );
            assertEquals( "observation nr: " + i + " has wrong content", contents.get( i ), response.getResponseText() );
            assertEquals( "observation nr: " + i + " is unexpected confirmable", expectConfirmable, response.advanced().isConfirmable() );
        }
        relation.reactiveCancel();
    }

    public class ReceivedNotification
    {
        public long ts;

        public CoapResponse response;

        public ReceivedNotification( CoapResponse response )
        {
            this.ts= System.currentTimeMillis();
            this.response= response;
        }

    }

}
