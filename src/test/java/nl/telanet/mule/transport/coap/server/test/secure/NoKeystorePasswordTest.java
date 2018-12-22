package nl.telanet.mule.transport.coap.server.test.secure;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.junit.internal.matchers.ThrowableCauseMatcher.hasCause;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

import java.io.IOException;
import java.security.UnrecoverableKeyException;

import org.mule.api.ConnectionException;
import org.mule.api.lifecycle.LifecycleException;

import nl.teslanet.mule.transport.coap.server.error.EndpointConstructionException;
import nl.teslanet.mule.transport.coap.server.test.utils.AbstractMuleStartTestCase;


public class NoKeystorePasswordTest extends AbstractMuleStartTestCase
{
    @Override
    protected void expectException()
    {
        exception.expect( isA( LifecycleException.class ) );
        exception.expect( hasMessage( containsString( "nl.teslanet.mule.transport.coap.server" ) ) );
        exception.expect( hasCause( isA( ConnectionException.class ) ) );
        exception.expect( hasCause( hasMessage( containsString( "CoAP configuration error" ) ) ) );
        exception.expect( hasCause( hasCause( isA( EndpointConstructionException.class ) ) ) );
        exception.expect( hasCause( hasCause( hasMessage( containsString( "cannot load keystore" ) ) ) ) );
        exception.expect( hasCause( hasCause( hasMessage( containsString( "certs/keyStore.jks" ) ) ) ) );
        exception.expect( hasCause( hasCause( hasMessage( containsString( "using passwd" ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( isA( IOException.class ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasMessage( containsString( "password was incorrect" ) ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasCause( isA( UnrecoverableKeyException.class ) ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasCause( hasMessage( containsString( "Password verification failed" ) ) ) ) ) ) );
    }

    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver3.xml";
    };
}