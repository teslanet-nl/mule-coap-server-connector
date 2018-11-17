package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public abstract class AbstractOutboundPropertyTestcase extends FunctionalMunitSuite
{
    URI uri= null;

    private static HashMap< Code, String > calls;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver1.xml";
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

    @BeforeClass
    static public void setUpClass() throws Exception
    {
        calls= new HashMap< Code, String >();
        calls.put( Code.GET, "/get_me" );
        calls.put( Code.PUT, "/put_me" );
        calls.put( Code.POST, "/post_me" );
        calls.put( Code.DELETE, "/delete_me" );
    }

    @Before
    public void setUp() throws Exception
    {
        uri= new URI( "coap", "127.0.0.1", null, null );
    }

    @After
    public void tearDown() throws Exception
    {
    }

    abstract protected Object fetchOption( OptionSet options );

    abstract protected String getPropertyName();

    abstract protected Object getPropertyValue() throws Exception;

    abstract protected Object getExpectedOptionValue() throws Exception;

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( "/service" + path ) );
        client.setTimeout( 1000L );
        return client;
    }

    private void mockMessage( String propertyName, Object propertyValue )
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( propertyName, propertyValue );

        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        MuleMessage messageToBeReturned= muleMessageWithPayload( "nothing important" );
        messageToBeReturned.addProperties( props, PropertyScope.OUTBOUND );
        mocker.thenReturn( messageToBeReturned );
    }

    @Test
    public void testOutbound() throws Exception
    {
        mockMessage( getPropertyName(), getPropertyValue() );

        for ( Entry< Code, String > entry : calls.entrySet() )
        {
            CoapClient client= getClient( entry.getValue() );
            Request request= new Request( entry.getKey() );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
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
            client.shutdown();
        }
    }

    protected boolean optionValueIsCollectionOfByteArray()
    {
        return false;
    }
}