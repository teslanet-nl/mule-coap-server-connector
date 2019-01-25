package nl.teslanet.mule.transport.coap.server.test.discovery;


import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.eclipse.californium.core.CoapClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.munit.runner.functional.FunctionalMunitSuite;


public class PingTest extends FunctionalMunitSuite
{
    URI uri= null;

    @Override
    protected String getConfigResources()
    {
        return "mule-config/discovery/testserver1.xml";
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
    }

    private CoapClient getClient( String path )
    {
        CoapClient client= new CoapClient( uri.resolve( path ) );
        client.setTimeout( 1000L );
        return client;
    }

    @Test
    public void testPing()
    {
        String path= "/service";
        CoapClient client= getClient( path );
        boolean reachable= client.ping( 1000 );
        assertTrue( "service is not reachable", reachable );
    }
}