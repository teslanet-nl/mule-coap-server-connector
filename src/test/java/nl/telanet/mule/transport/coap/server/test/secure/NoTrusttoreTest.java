package nl.telanet.mule.transport.coap.server.test.secure;


import org.mule.api.ConnectionException;
import org.mule.api.lifecycle.LifecycleException;

import nl.teslanet.mule.transport.coap.server.error.EndpointConstructionException;
import nl.teslanet.mule.transport.coap.server.test.config.AbstractMuleStartTestCase;
import nl.teslanet.mule.transport.coap.server.test.utils.CausalClassChainMatcher;


public class NoTrusttoreTest extends AbstractMuleStartTestCase
{
    @Override
    protected void expectException()
    {
        exception.expect( LifecycleException.class );
        exception.expectMessage( "nl.teslanet.mule.transport.coap.server" );
        exception.expectCause( new CausalClassChainMatcher( ConnectionException.class, EndpointConstructionException.class, EndpointConstructionException.class ) );
    }

    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver5.xml";
    };
}