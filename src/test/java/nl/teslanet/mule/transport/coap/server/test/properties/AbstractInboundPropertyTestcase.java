package nl.teslanet.mule.transport.coap.server.test.properties;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.mule.munit.common.mocking.SpyProcess;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public abstract class AbstractInboundPropertyTestcase extends FunctionalMunitSuite
{
    URI uri= null;

    CoapClient client= null;

    private boolean spyActivated;

    private static ArrayList< Code > calls;
    private static HashMap< Code, String > paths;

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
        calls= new ArrayList< Code >();
        calls.add( Code.GET );
        calls.add( Code.PUT );
        calls.add( Code.POST );
        calls.add( Code.DELETE );
        
        paths= new HashMap< Code, String >();
        paths.put( Code.GET, "/service/get_me" );
        paths.put( Code.PUT, "/service/put_me" );
        paths.put( Code.POST, "/service/post_me" );
        paths.put( Code.DELETE, "/service/delete_me" );
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

    abstract protected void addOption( OptionSet options ) throws Exception;

    abstract protected String getPropertyName();

    abstract protected Object getExpectedPropertyValue() throws Exception;

    protected Boolean propertyValueIsByteArray()
    {
        return Boolean.FALSE;
    }
    
    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 2000L );
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
                    if ( propertyValueIsByteArray())
                    {
                        assertArrayEquals( "property has wrong value", (byte[])expected, (byte[])prop );
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

    @Test(timeout=2000L)
    public void testInbound() throws Exception
    {
        spyMessage( getPropertyName(), getExpectedPropertyValue() );

        for ( Code call : calls )
        {
            spyActivated= false;
            CoapClient client= getClient( getPath( call ) );
            Request request= new Request( call );
            addOption( request.setPayload( "nothing important" ).getOptions() );

            CoapResponse response= client.advanced( request );

            assertNotNull( "get gave no response", response );
            assertTrue( "response indicates failure", response.isSuccess() );
            assertTrue( "spy was not activated", spyActivated );
            
            client.shutdown();
        }
    }

    protected String getPath( Code call )
    {
        return paths.get( call );
    }
}