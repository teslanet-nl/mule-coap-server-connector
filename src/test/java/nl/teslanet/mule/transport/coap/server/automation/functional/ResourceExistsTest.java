package nl.teslanet.mule.transport.coap.server.automation.functional;


import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;


public class ResourceExistsTest extends AbstractCoapTestCase
{

    public ResourceExistsTest()
    {
        super(  );
    }

    @Ignore
    @Test
    public void verify() throws ResourceUriException
    {
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
        assertEquals( getConnector().ResourceExists( uri ), false );
        getConnector().addResource( uri, get, put, post, delete, observable, earlyAck, title, ifdesc, rt, sz, ct );
        assertEquals( getConnector().ResourceExists( uri ), true );
        getConnector().removeResource( uri );
        assertEquals( getConnector().ResourceExists( uri ), false );
    }

}