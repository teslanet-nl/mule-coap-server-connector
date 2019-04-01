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
import java.util.HashMap;
import java.util.Iterator;

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
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


/**
 * Test for outbound properties
 *
 */
@RunWith(Parameterized.class)

public abstract class AbstractOutboundPropertyTestcase extends FunctionalMunitSuite
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
     * Implement to specify how option should be retrieved from optionset
     * @param options where to fetch the option from
     * @return
     */
    abstract protected Object fetchOption( OptionSet options );

    /**
     * Implement to specify the outbound property that should be set
     * @return the name of the outbound property to test
     */
    abstract protected String getPropertyName();

    /**
     * Implement to specify the outbound property value to set
     * @return the value of the outbound property to set
     */
    abstract protected Object getPropertyValue() throws Exception;

    /**
     * Implement to specify the option value to expect in the response
     * @return the expected value
     */
    abstract protected Object getExpectedOptionValue() throws Exception;

    /**
     * Override to specify whether the option is an Collection of ByteArray
     * @return {@code true} when option is a Collection ByteArray
     */
    protected boolean optionValueIsCollectionOfByteArray()
    {
        return false;
    }

    /**
     * Mock that sets the outbound property in the Mule flow
     * @param propertyName name of the outbound property to set
     * @param propertyValue value to set on the property
     */
    private void mockMessage( String propertyName, Object propertyValue )
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( propertyName, propertyValue );

        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        MuleMessage messageToBeReturned= muleMessageWithPayload( "nothing important" );
        messageToBeReturned.addProperties( props, PropertyScope.OUTBOUND );
        mocker.thenReturn( messageToBeReturned );
    }

    /**
     * Test outbound property
     * @throws Exception
     */
    @Test
    public void testOutbound() throws Exception
    {
        mockMessage( getPropertyName(), getPropertyValue() );

        Request request= new Request( requestCode );
//        if ( unintendedPayload ) request.setUnintendedPayload();
        request.setPayload( "nothing important" );

        CoapResponse response= client.advanced( request );

        assertNotNull( "no response received", response );
        assertTrue( "response indicates failure", response.isSuccess() );

        if ( optionValueIsCollectionOfByteArray() )
        {
            @SuppressWarnings("unchecked")
            Collection< byte[] > option= (Collection< byte[] >) fetchOption( response.getOptions() );

            @SuppressWarnings("unchecked")
            Collection< byte[] > expected= (Collection< byte[] >) getExpectedOptionValue();
            assertEquals( "option value list length differ", expected.size(), option.size() );

            Iterator< byte[] > optionIt= option.iterator();
            Iterator< byte[] > expectedIt= expected.iterator();
            while ( optionIt.hasNext() && expectedIt.hasNext() )
            {
                byte[] optionValue= optionIt.next();
                byte[] expectedValue= expectedIt.next();
                assertArrayEquals( "value in collection not equal", expectedValue, optionValue );
            } ;
        }
        else
        {
            assertEquals( "option has wrong value", getExpectedOptionValue(), fetchOption( response.getOptions() ) );
        }
    }
}