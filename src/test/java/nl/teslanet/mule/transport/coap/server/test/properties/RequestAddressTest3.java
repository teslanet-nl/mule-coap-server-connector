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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class RequestAddressTest3 extends FunctionalMunitSuite
{
    URI uri= null;

    CoapClient client= null;

    String expected= "/0:0:0:0:0:0:0:1:8883";

    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver-RequestAddress3.xml";
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
        uri= new URI( "coap://[::1]:8883" );
    }

    @After
    public void tearDown() throws Exception
    {
        if ( client != null ) client.shutdown();
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( "/requestaddress" + path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test
    public void testGet()
    {
        String path= "/get_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.get();

        assertNotNull( "get gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request address has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testPut()
    {
        String path= "/put_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.put( "nothing important", 0 );

        assertNotNull( "put gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request address has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testPost()
    {
        String path= "/post_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.post( "nothing important", 0 );

        assertNotNull( "post gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request address has wrong value", expected, response.getResponseText() );
    }

    @Test
    public void testDelete()
    {
        String path= "/delete_me";

        CoapClient client= getClient( path );

        CoapResponse response= client.delete();

        assertNotNull( "delete gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertEquals( "echoed request address has wrong value", expected, response.getResponseText() );
    }
}