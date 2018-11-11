package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.HashMap;
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
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.common.mocking.MessageProcessorMocker;
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public abstract class AbstractInboundOutboundPropertyTestcase< T > extends FunctionalMunitSuite
{
    URI uri= null;

    CoapClient client= null;

    private boolean spyActivated;

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
        if ( client != null ) client.shutdown();
    }

    abstract protected void addOption( OptionSet options );

    abstract protected Object fetchOption( OptionSet options );

    abstract protected String getPropertyName();

    abstract protected Object getPropertyValue();
    
    abstract protected Object getExpectedPropertyValue();

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( "/service" + path ) );
        client.setTimeout( 1000L );
        return client;
    }

    private void spyMessage( final String propertyName, final Object expected )
    {
        SpyProcess beforeSpy= new SpyProcess()
            {
                @Override
                public void spy( MuleEvent event ) throws MuleException
                {
                    Object prop= event.getMessage().getInboundProperty( propertyName );
                    assertEquals( "property has wrong class", expected.getClass(), prop.getClass() );
                    assertEquals( "property has wrong value", expected, prop );
                    spyActivated= true;
                }
            };

        spyMessageProcessor( "set-payload" ).ofNamespace( "mule" ).before( beforeSpy );
    }

    private void injectMessage( String propertyName, Object propertyValue )
    {
        HashMap< String, Object > props= new HashMap< String, Object >();
        props.put( propertyName, propertyValue );

        MessageProcessorMocker mocker= whenMessageProcessor( "set-payload" ).ofNamespace( "mule" );
        MuleMessage messageToBeReturned= muleMessageWithPayload( "nothing important" );
        messageToBeReturned.addProperties( props, PropertyScope.OUTBOUND );
        mocker.thenReturn( messageToBeReturned );
    }

    @Test
    public void testInbound()
    {
        spyMessage( getPropertyName(), getExpectedPropertyValue() );

        for ( Entry< Code, String > entry : calls.entrySet() )
        {
            spyActivated= false;
            CoapClient client= getClient( entry.getValue() );
            Request request= new Request( entry.getKey() );
            addOption( request.setPayload( "nothing important" ).getOptions() );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure", response.isSuccess() );
            assertTrue( "spy was not activated", spyActivated );
        }
    }

    @Test
    public void testOutbound1()
    {
        injectMessage( getPropertyName(), getPropertyValue() );

        for ( Entry< Code, String > entry : calls.entrySet() )
        {
            CoapClient client= getClient( entry.getValue() );
            Request request= new Request( entry.getKey() );
            request.setPayload( "nothing important" );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure", response.isSuccess() );
            assertEquals( "option has wrong value", getExpectedPropertyValue(), fetchOption( response.getOptions() ) );
        }
    }
}