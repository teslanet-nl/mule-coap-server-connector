/*******************************************************************************
 * Copyright (c) 2017, 2018, 2019 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License - v 2.0 
 * which accompanies this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/
package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class RequestConfirmableTest extends FunctionalMunitSuite
{
    URI uri= null;

    CoapClient client= null;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver-RequestConfirmable.xml";
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
        CoapClient client= new CoapClient( uri.resolve( "/requestconfirmable" + path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test
    public void testGet()
    {
        String path= "/get_me";
        String expected= "true";

        CoapClient client= getClient( path );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    //CF106bug
    @Ignore
    @Test
    public void testGetNon()
    {
        String path= "/get_me";
        String expected= "false";

        CoapClient client= getClient( path );
        Request request= new Request( Code.GET, Type.NON );
        CoapResponse response= client.advanced( request );

        assertNotNull( "get gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testPut()
    {
        String path= "/put_me";
        String expected= "true";

        CoapClient client= getClient( path );

        CoapResponse response= client.put( "nothing important", 0 );

        assertNotNull( "put gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    //CF106bug
    @Ignore
    @Test
    public void testPutNon()
    {
        String path= "/put_me";
        String expected= "false";

        CoapClient client= getClient( path );
        Request request= new Request( Code.PUT, Type.NON );
        CoapResponse response= client.advanced( request );

        assertNotNull( "put gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testPost()
    {
        String path= "/post_me";
        String expected= "true";

        CoapClient client= getClient( path );

        CoapResponse response= client.post( "nothing important", 0 );

        assertNotNull( "post gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    //CF106bug
    @Ignore
    @Test
    public void testPostNon()
    {
        String path= "/post_me";
        String expected= "false";

        CoapClient client= getClient( path );
        Request request= new Request( Code.POST, Type.NON );
        CoapResponse response= client.advanced( request );

        assertNotNull( "post gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testDelete()
    {
        String path= "/delete_me";
        String expected= "true";

        CoapClient client= getClient( path );

        CoapResponse response= client.delete();

        assertNotNull( "delete gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }

    //CF106bug
    @Ignore
    @Test
    public void testDeleteNon()
    {
        String path= "/delete_me";
        String expected= "false";

        CoapClient client= getClient( path );
        Request request= new Request( Code.DELETE, Type.NON );
        CoapResponse response= client.advanced( request );

        assertNotNull( "delete gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request confirmable has wrong value", expected, response.getResponseText() );
    }
}