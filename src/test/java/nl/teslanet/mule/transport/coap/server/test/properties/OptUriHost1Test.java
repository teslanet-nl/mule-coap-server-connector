package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO configuration option isProxy and automatic  5.05 (Proxying Not Supported (when false)) 
public class OptUriHost1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setUriHost( "some.server.org" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_host";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new String( "some.server.org" );
    }

}