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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * Test for inbound properties
 *
 */
@RunWith(Parameterized.class)
public abstract class AbstractInboundPropertyTestcase extends FunctionalMunitSuite
{
    @Parameters(name= "Request= {0}, path= {2}")
    public static Collection< Object[] > data()
    {
        return Arrays.asList(
            new Object [] []{
                //default maxResourceBodySize on server
                { Code.GET, 5683, "/service/get_me", true },
                { Code.PUT, 5683, "/service/put_me", false },
                { Code.POST, 5683, "/service/post_me", false },
                { Code.DELETE, 5683, "/service/delete_me", true }, } );
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
     * Test client that issues requests
     */
    CoapClient client= null;

    /**
     * Flag indicating the message processor that is spied on was activated 
     */
    private boolean spyActivated;

    /* (non-Javadoc)
     * @see org.mule.munit.runner.functional.FunctionalMunitSuite#getConfigResources()
     */
    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver1.xml";
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

    /**
     * Implement to add the option to test to the optionset
     * @param options where to add the opton
     * @throws Exception when option could not be set
     */
    abstract protected void addOption( OptionSet options ) throws Exception;

    /**
     * Implement to specify the property that should be spied on
     * @return the name of the inbound property to test
     */
    abstract protected String getPropertyName();

    /**
     * Implement to specify the option value the test should deliver
     * @return the expected option value
     * @throws Exception 
     */
    abstract protected Object getExpectedPropertyValue() throws Exception;

    /**
     * Override to specify whether the option value is a byte array
     * @return {@code true} when the option value is a byte array
     */
    protected Boolean propertyValueIsByteArray()
    {
        return Boolean.FALSE;
    }

    /**
     * The spy to assert the inbound property 
     * @param propertyName name of the property to inspect
     * @param expected
     */
    private void spyMessage( final String propertyName, final Object expected )
    {
        SpyProcess beforeSpy= new SpyProcess()
            {
                @Override
                public void spy( MuleEvent event ) throws MuleException
                {
                    Object prop= event.getMessage().getInboundProperty( propertyName );
                    assertEquals( "property has wrong class", expected.getClass(), prop.getClass() );
                    if ( propertyValueIsByteArray() )
                    {
                        assertArrayEquals( "property has wrong value", (byte[]) expected, (byte[]) prop );
                    }
                    else
                    {
                        assertEquals( "property has wrong value", expected, prop );
                    }
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    /**
     * Test inbound property
     * @throws Exception 
     */
    @Test(timeout= 2000L)
    public void testInbound() throws Exception
    {
        spyMessage( getPropertyName(), getExpectedPropertyValue() );

        spyActivated= false;
        Request request= new Request( requestCode );
//        if ( unintendedPayload ) request.setUnintendedPayload();
        addOption( request.setPayload( "nothing important" ).getOptions() );

        CoapResponse response= client.advanced( request );

        assertNotNull( "get gave no response", response );
        assertTrue( "response indicates failure", response.isSuccess() );
        assertTrue( "spy was not activated", spyActivated );
    }
}