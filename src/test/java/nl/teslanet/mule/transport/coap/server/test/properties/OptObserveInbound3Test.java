package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptObserveInbound3Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setObserve( 154 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.observe";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 154 );
    }

}