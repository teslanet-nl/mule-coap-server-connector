/**
 * 
 */
package nl.teslanet.mule.transport.coap.server.automation.functional;

import org.junit.After;
import org.junit.Before;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import nl.teslanet.mule.transport.coap.server.CoapServerConnector;

/**
 * @author rogier
 *
 */
public class AbstractCoapTestCase extends AbstractTestCase< CoapServerConnector >
{
    public AbstractCoapTestCase( )
    {
        super( CoapServerConnector.class );
    }

    @Before
    public void setup()
    {
        // nop
    }

    @After
    public void tearDown()
    {
        // nop
    }

}
