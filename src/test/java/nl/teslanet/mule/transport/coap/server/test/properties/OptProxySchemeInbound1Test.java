package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO add observe functionality test
public class OptProxySchemeInbound1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setProxyScheme( "coapsx" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.proxy_scheme";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new String( "coapsx" );
    }

}