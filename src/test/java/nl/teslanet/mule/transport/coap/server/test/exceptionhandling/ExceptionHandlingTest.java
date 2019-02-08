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
package nl.teslanet.mule.transport.coap.server.test.exceptionhandling;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * Test for inbound properties
 *
 */
@RunWith(Parameterized.class)
public class ExceptionHandlingTest extends FunctionalMunitSuite
{
    /**
     * The type of exceptionhandling on the server
     *
     */
    public enum ExceptionHandling
    {
        HANDLED, UNHANDLED, NO_HANDLER
    }

    /**
     * @return the test parameters
     */
    @Parameters(name= "Request= {0}, path= {2}, exceptionHandling= {5}")
    public static Collection< Object[] > data()
    {
        return Arrays.asList(
            new Object [] []{
                //default maxResourceBodySize on server
                { Code.GET, 5683, "/service/handled_exception", true, ResponseCode.CONTENT, ExceptionHandling.HANDLED },
                { Code.PUT, 5683, "/service/handled_exception", false, ResponseCode.CREATED, ExceptionHandling.HANDLED },
                { Code.POST, 5683, "/service/handled_exception", false, ResponseCode.CHANGED, ExceptionHandling.HANDLED },
                { Code.DELETE, 5683, "/service/handled_exception", true, ResponseCode.DELETED, ExceptionHandling.HANDLED },
                { Code.GET, 5683, "/service/unhandled_exception", true, ResponseCode.CONTENT, ExceptionHandling.UNHANDLED },
                { Code.PUT, 5683, "/service/unhandled_exception", false, ResponseCode.CREATED, ExceptionHandling.UNHANDLED },
                { Code.POST, 5683, "/service/unhandled_exception", false, ResponseCode.CHANGED, ExceptionHandling.UNHANDLED },
                { Code.DELETE, 5683, "/service/unhandled_exception", true, ResponseCode.DELETED, ExceptionHandling.UNHANDLED },
                { Code.GET, 5683, "/service/no_handler", true, ResponseCode.CONTENT, ExceptionHandling.NO_HANDLER },
                { Code.PUT, 5683, "/service/no_handler", false, ResponseCode.CREATED, ExceptionHandling.NO_HANDLER },
                { Code.POST, 5683, "/service/no_handler", false, ResponseCode.CHANGED, ExceptionHandling.NO_HANDLER },
                { Code.DELETE, 5683, "/service/no_handler", true, ResponseCode.DELETED, ExceptionHandling.NO_HANDLER }, } );
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
     * True when request is not supposed to have a payload, but does
     */
    @Parameter(3)
    public boolean unintendedPayload;

    /**
     * Expected response code to test
     */
    @Parameter(4)
    public ResponseCode ExpectedResponseCode;

    /**
     * Exceptionhandling used in the test
     */
    @Parameter(5)
    public ExceptionHandling exceptionHandling;

    /**
     * Test client that issues requests
     */
    CoapClient client= null;

    /* (non-Javadoc)
     * @see org.mule.munit.runner.functional.FunctionalMunitSuite#getConfigResources()
     */
    @Override
    protected String getConfigResources()
    {
        return "mule-config/exceptionhandling/testserver1.xml";
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
        client= new CoapClient( new URI( "coap", "127.0.0.1", resourcePath, null, null ) );
        client.setTimeout( 1000L );
    }

    /**
     * shutdown test client
     * @throws Exception
     */
    @After
    public void tearDownClient() throws Exception
    {
        if ( client != null ) client.shutdown();
    }

    @Test(timeout= 2000L)
    public void testException() throws Exception
    {
        Request request= new Request( requestCode );
        if ( unintendedPayload ) request.setUnintendedPayload();
        request.setPayload( "nothing important" );

        CoapResponse response= client.advanced( request );
        switch ( exceptionHandling )
        {
            case HANDLED:
                assertNotNull( "request gave no response", response );
                assertTrue( "response indicates failure", response.isSuccess() );
                assertEquals( "wrong responsecode", ExpectedResponseCode, response.getCode() );
                assertEquals( "wrong response payload", "exception catched", response.getResponseText() );
                break;
            case UNHANDLED:
                assertNotNull( "request gave no response", response );
                assertFalse( "response does not indicate failure", response.isSuccess() );
                assertEquals( "wrong responsecode", ResponseCode.INTERNAL_SERVER_ERROR, response.getCode() );
                assertEquals( "wrong response payload", "EXCEPTION IN PROCESSING FLOW", response.getResponseText() );
                break;
            case NO_HANDLER:
                assertNotNull( "request gave no response", response );
                assertFalse( "response does not indicate failure", response.isSuccess() );
                assertEquals( "wrong responsecode", ResponseCode.INTERNAL_SERVER_ERROR, response.getCode() );
                assertEquals( "wrong response payload", "NO LISTENER", response.getResponseText() );
                break;
            default:
                break;
        }
    }
}