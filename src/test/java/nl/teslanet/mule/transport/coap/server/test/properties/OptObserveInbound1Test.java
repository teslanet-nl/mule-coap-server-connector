package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO add observe functionality test
public class OptObserveInbound1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setObserve( 0 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.observe";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 0 );
    }

}