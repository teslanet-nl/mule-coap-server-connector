package nl.teslanet.mule.transport.coap.server.modules.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mule.api.callback.SourceCallback;

import nl.teslanet.mule.transport.coap.server.Listener;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;
import nl.teslanet.mule.transport.coap.server.generated.sources.ListenMessageSource;


public class ListenerTestCases
{

    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {

    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testConstructor() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        uri= "/some_resource";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child/child";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "some_resource";
        listener= new Listener( uri, callback );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/child";
        listener= new Listener( uri, callback );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/child/child";
        listener= new Listener( uri, callback );
        assertListener( listener, "/" + uri, callback );

        uri= "/*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "/some_resource/*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child/*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "*";
        listener= new Listener( uri, callback );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/*";
        listener= new Listener( uri, callback );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/child/*";
        listener= new Listener( uri, callback );
        assertListener( listener, "/" + uri, callback );

        uri= "/some_resource*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child/child*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriNull() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "null is not allowed" );

        uri= null;
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriEmpty1() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "uri cannot be empty" );
        uri= "";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriEmpty2() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "uri cannot be empty" );
        uri= "/";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriMultipleWildcard1() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "wildcard needs to be last character" );
        uri= "**";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriMultipleWildcard2() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "wildcard needs to be last character" );
        uri= "/some_resource/**";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriMultipleWildcard3() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "wildcard needs to be last character" );
        uri= "/some_resource/*/*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriMultipleWildcard4() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "wildcard needs to be last character" );
        uri= "/some_resource/*/child/*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriMultipleWildcard5() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "wildcard needs to be last character" );
        uri= "/some_resource/*/child";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testConstructorWithInvalidUriMultipleWildcard6() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation" );
        String uri;
        Listener listener;

        exception.expect( ResourceUriException.class );
        exception.expectMessage( "Invalid CoAP resource uri" );
        exception.expectMessage( "wildcard needs to be last character" );
        uri= "/some_resource*/child/*";
        listener= new Listener( uri, callback );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testSetUri() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation2" );
        String uri= "/initial";
        Listener listener= new Listener( uri, callback );

        uri= "/some_resource";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child/child";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "some_resource";
        listener.setUri( uri );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/child";
        listener.setUri( uri );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/child/child";
        listener.setUri( uri );
        assertListener( listener, "/" + uri, callback );

        uri= "/*";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "/some_resource/*";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child/*";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "*";
        listener.setUri( uri );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/*";
        listener.setUri( uri );
        assertListener( listener, "/" + uri, callback );

        uri= "some_resource/child/*";
        listener.setUri( uri );
        assertListener( listener, "/" + uri, callback );

        uri= "/some_resource*";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child*";
        listener.setUri( uri );
        assertListener( listener, uri, callback );

        uri= "/some_resource/child/child*";
        listener.setUri( uri );
        assertListener( listener, uri, callback );
    }

    @Test
    public void testSetCallback() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation3" );
        String uri= "/initial";
        Listener listener= new Listener( uri, callback );

        callback= new ListenMessageSource( "operation4" );
        listener.setCallback( callback );
        assertListener( listener, uri, callback );

        callback= new ListenMessageSource( "operation5" );
        listener.setCallback( callback );
        assertListener( listener, uri, callback );
    }

    private void assertListener( Listener listener, String uri, SourceCallback callback )
    {
        assertNotNull( "listener construction failed", listener );
        assertEquals( "listener uri has wrong value", uri, listener.getUri() );
        assertEquals( "listener callback has wrong value", callback, listener.getCallback() );

    }
}
