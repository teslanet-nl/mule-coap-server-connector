package nl.teslanet.mule.transport.coap.server.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;


public class GetMethodTestCases extends FunctionalTestCase
{
    CoapClient client1= null;

    CoapClient client2= null;

    CoapClient client3= null;

    CoapClient client4= null;

    @Override
    protected String getConfigFile()
    {
        return "mule-config/testserver1.xml";
    };

    @Before
    public void setUp() throws Exception
    {
        client1= new CoapClient( "coap://127.0.0.1/get_me" );
        client1.setTimeout( 1000L );
        client2= new CoapClient( "coap://127.0.0.1/do_not_get_me" );
        client2.setTimeout( 1000L );
        client3= new CoapClient( "coap://127.0.0.1/get_me2" );
        client3.setTimeout( 1000L );
        client4= new CoapClient( "coap://127.0.0.1/do_not_get_me3" );
        client4.setTimeout( 1000L );

    }

    @After
    public void tearDown() throws Exception
    {
        if ( client1 != null ) client1.shutdown();
        if ( client2 != null ) client2.shutdown();
        if ( client3 != null ) client3.shutdown();
        if ( client4 != null ) client4.shutdown();
    }

    @Test
    public void testSuccess()
    {
        CoapResponse response= client1.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be CONTENT", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "response payload has wrong value", "/get_me", response.getResponseText() );
    }
    @Test
    public void testNoGetAllowed()
    {
        CoapResponse response= client2.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be METHOD_NOT_ALLOWED", ResponseCode.METHOD_NOT_ALLOWED, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
    }
    @Test
    public void testSuccess2()
    {
        CoapResponse response= client3.get();

        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be CONTENT", ResponseCode.CONTENT, response.getCode() );
        assertEquals( "response payload has wrong value", "/get_me2", response.getResponseText() );
    }

    @Test
    public void testNoResource()
    {
        CoapResponse response= client4.get();
    
        assertNotNull( "get gave no response", response );
        assertEquals( "response code should be NOT_FOUND", ResponseCode.NOT_FOUND, response.getCode() );
        assertEquals( "response payload has wrong value", "", response.getResponseText() );
}}