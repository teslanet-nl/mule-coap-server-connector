/*******************************************************************************
 * Copyright (c) 2017, 2018, 2019 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License - v 2.0 
 * which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/
package nl.teslanet.mule.transport.coap.server.test.resources;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * Test adding a resource 
 *
 */
@RunWith(Parameterized.class)
public class AddResourceTest extends FunctionalMunitSuite
{
    /**
     * @return the test parameters
     */
    @Parameters(name= "Request= {0}, path= {2}, addResource= {3}")
    public static Collection< Object[] > data()
    {
        return Arrays.asList(
            new Object [] []{
                //default maxResourceBodySize on server
                { Code.GET, 5683, "/service/new_get_resource1", "/service/add_resource/all_methods", ResponseCode.CONTENT },
                { Code.PUT, 5683, "/service/new_put_resource1", "/service/add_resource/all_methods", ResponseCode.CHANGED },
                { Code.POST, 5683, "/service/new_post_resource1", "/service/add_resource/all_methods", ResponseCode.CHANGED },
                { Code.DELETE, 5683, "/service/new_delete_resource1", "/service/add_resource/all_methods", ResponseCode.DELETED },
                { Code.GET, 5683, "/service/new_get_resource2", "/service/add_resource/get_only", ResponseCode.CONTENT },
                { Code.PUT, 5683, "/service/new_put_resource2", "/service/add_resource/get_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.POST, 5683, "/service/new_post_resource2", "/service/add_resource/get_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.DELETE, 5683, "/service/new_delete_resource2", "/service/add_resource/get_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.GET, 5683, "/service/new_get_resource3", "/service/add_resource/post_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.PUT, 5683, "/service/new_put_resource3", "/service/add_resource/post_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.POST, 5683, "/service/new_post_resource3", "/service/add_resource/post_only", ResponseCode.CHANGED },
                { Code.DELETE, 5683, "/service/new_delete_resource3", "/service/add_resource/post_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.GET, 5683, "/service/new_get_resource4", "/service/add_resource/put_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.PUT, 5683, "/service/new_put_resource4", "/service/add_resource/put_only", ResponseCode.CHANGED },
                { Code.POST, 5683, "/service/new_post_resource4", "/service/add_resource/put_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.DELETE, 5683, "/service/new_delete_resource4", "/service/add_resource/put_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.GET, 5683, "/service/new_get_resource5", "/service/add_resource/delete_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.PUT, 5683, "/service/new_put_resource5", "/service/add_resource/delete_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.POST, 5683, "/service/new_post_resource5", "/service/add_resource/delete_only", ResponseCode.METHOD_NOT_ALLOWED },
                { Code.DELETE, 5683, "/service/new_delete_resource5", "/service/add_resource/delete_only", ResponseCode.DELETED } } );
    }

    /**
     * Request code to test
     */
    @Parameter(0)
    public Code requestCode;

    /**
     * Test server port
     */
    @Parameter(1)
    public int port;

    /**
    * Test resource to call
    */
    @Parameter(2)
    public String resourcePath;

    /**
    * Resource to call for adding
    */
    @Parameter(3)
    public String addResourcePath;

    /**
     * Expected response code to test
     */
    @Parameter(4)
    public ResponseCode expectedResponseCode;

    /**
     * Test client that issues requests
     */
    CoapClient client1= null;

    /**
     * Client that adds resource
     */
    CoapClient client2= null;

    /* (non-Javadoc)
     * @see org.mule.munit.runner.functional.FunctionalMunitSuite#getConfigResources()
     */
    @Override
    protected String getConfigResources()
    {
        return "mule-config/resources/testserver1.xml";
    };

    /* (non-Javadoc)
     * @see org.mule.munit.runner.functional.FunctionalMunitSuite#haveToDisableInboundEndpoints()
     */
    @Override
    protected boolean haveToDisableInboundEndpoints()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see org.mule.munit.runner.functional.FunctionalMunitSuite#haveToMockMuleConnectors()
     */
    @Override
    protected boolean haveToMockMuleConnectors()
    {
        return false;
    }

    /**
     * Create test client
     * @throws Exception
     */
    @Before
    public void setUpClient() throws Exception
    {
        client1= new CoapClient( new URI( "coap", "127.0.0.1", resourcePath, null, null ) );
        client1.setTimeout( 1000L );
        client2= new CoapClient( new URI( "coap", "127.0.0.1", addResourcePath, null, null ) );
        client2.setTimeout( 1000L );
    }

    /**
     * shutdown test client
     * @throws Exception
     */
    @After
    public void tearDownClient() throws Exception
    {
        if ( client1 != null ) client1.shutdown();
        if ( client2 != null ) client2.shutdown();
    }

    /**
     * Test adding a resource on the server
     * @throws Exception
     */
    @Test(timeout= 2000000L)
    public void testAddResource() throws Exception
    {
        Request request= new Request( this.requestCode );
        CoapResponse response1= client1.advanced( request );
        assertNotNull( "request gave no response", response1 );
        assertEquals( "request gave wrong response", ResponseCode.NOT_FOUND, response1.getCode() );

        Request request2= new Request( Code.POST );
        request2.setPayload( "some content" );
        request2.getOptions().setLocationPath( resourcePath );
        CoapResponse response2= client2.advanced( request2 );
        assertNotNull( "post gave no response", response2 );
        assertTrue( "post response indicates failure", response2.isSuccess() );
        assertEquals( "post gave wrong response", ResponseCode.CREATED, response2.getCode() );

        Request request3= new Request( this.requestCode );
        CoapResponse response3= client1.advanced( request3 );
        assertNotNull( "got no response", response3 );
        assertEquals( "wrong response", expectedResponseCode, response3.getCode() );
    }
}
