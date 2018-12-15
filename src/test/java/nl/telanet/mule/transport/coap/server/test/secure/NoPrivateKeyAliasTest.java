package nl.telanet.mule.transport.coap.server.test.secure;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.junit.internal.matchers.ThrowableCauseMatcher.hasCause;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

import org.mule.api.ConnectionException;
import org.mule.api.lifecycle.LifecycleException;

import nl.teslanet.mule.transport.coap.server.error.EndpointConstructionException;
import nl.teslanet.mule.transport.coap.server.test.config.AbstractMuleStartTestCase;


public class NoPrivateKeyAliasTest extends AbstractMuleStartTestCase
{
    @Override
    protected void expectException()
    {
        exception.expect( isA( LifecycleException.class ) );
        exception.expect( hasMessage( containsString( "nl.teslanet.mule.transport.coap.server" ) ) );
        exception.expect( hasCause( isA( ConnectionException.class ) ) );
        exception.expect( hasCause( hasMessage( containsString( "CoAP configuration error" ) ) ) );
        exception.expect( hasCause( hasCause( isA( EndpointConstructionException.class ) ) ) );
        exception.expect( hasCause( hasCause( hasMessage( containsString( "cannot load truststore" ) ) ) ) );
        exception.expect( hasCause( hasCause( hasMessage( containsString( "certs/trustStore.jks" ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( isA( EndpointConstructionException.class ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasMessage( containsString( "identity with private key" ) ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasMessage( containsString( "serverNONEXISTENT" ) ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasMessage( containsString( "could not be set" ) ) ) ) ) );
    }

    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver4.xml";
    };
}