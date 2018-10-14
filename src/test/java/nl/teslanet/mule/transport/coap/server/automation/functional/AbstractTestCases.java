/**
 * 
 */
package nl.teslanet.mule.transport.coap.server.automation.functional;

import static org.junit.Assert.*;
import nl.teslanet.mule.transport.coap.server.CoapServerConnector;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

/**
 * @author rogier
 *
 */
public class AbstractTestCases extends AbstractTestCase< CoapServerConnector >
{
    public AbstractTestCases( )
    {
        super( CoapServerConnector.class );
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

}
