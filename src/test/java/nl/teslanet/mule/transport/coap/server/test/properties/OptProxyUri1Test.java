package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO configuration option isProxy and automatic  5.05 (Proxying Not Supported (when false)) 
public class OptProxyUri1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setProxyUri( "coap://some.server.org/test" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.proxy_uri";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new String( "coap://some.server.org/test" );
    }

}