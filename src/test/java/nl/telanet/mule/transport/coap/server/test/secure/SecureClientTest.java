package nl.telanet.mule.transport.coap.server.test.secure;

public class SecureClientTest extends AbstractSecureClientTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver1.xml";
    };
}