package nl.teslanet.mule.transport.coap.server.automation.functional;


import static org.junit.Assert.*;
import nl.teslanet.mule.transport.coap.server.CoapServerConnector;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;


public class AddResourceTest extends AbstractTestCases
{

    public AddResourceTest()
    {
        super( );
    }

    @Before
    public void setup()
    {
        // TODO
    }

    @After
    public void tearDown()
    {
        // TODO
    }

    @Ignore
    @Test
    public void verify() throws ResourceUriException
    {
        //void expected;
        String uri= "/test";
        boolean get= true;
        boolean put= true;
        boolean post= true;
        boolean delete= true;
        boolean observable= true;
        boolean earlyAck= false;
        java.lang.String title= "test resource";
        java.lang.String ifdesc= null;
        java.lang.String rt= null;
        java.lang.String sz= null;
        java.lang.String ct= null;
        
        getConnector().addResource( uri, get, put, post, delete, observable, earlyAck, title, ifdesc, rt, sz, ct );
        assertEquals( getConnector().ResourceExists( uri ), true );
    }

}