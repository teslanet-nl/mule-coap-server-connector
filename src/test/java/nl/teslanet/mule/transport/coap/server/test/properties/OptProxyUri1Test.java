package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO add observe functionality test
public class OptProxyUri1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setProxyUri( "coap://californium.eclipse.org/test" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.proxy_uri";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new String( "coap://californium.eclipse.org/test" );
    }

}