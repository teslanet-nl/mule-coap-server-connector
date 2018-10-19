package nl.teslanet.mule.transport.coap.server.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

import nl.teslanet.mule.transport.coap.server.CoapServerConnector;


//@RunWith(Suite.class)
//@SuiteClasses
//({ 
//    ResourceExistsTest.class,
//    AddResourceTest.class,
//    RemoveResourceTest.class,
//    ResourceChangedTest.class 
//})
public class FunctionalTestSuite
{

    @BeforeClass
    public static void initialiseSuite()
    {
        ConnectorTestContext.initialize( CoapServerConnector.class );
    }

    @AfterClass
    public static void shutdownSuite()
    {
        ConnectorTestContext.shutDown();
    }

}