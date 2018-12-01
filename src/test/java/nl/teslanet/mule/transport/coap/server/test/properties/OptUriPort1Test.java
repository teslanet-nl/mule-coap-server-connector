package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO configuration option isProxy and automatic  5.05 (Proxying Not Supported (when false)) 
public class OptUriPort1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setUriPort( 4556 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_port";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 4556 );
    }

}