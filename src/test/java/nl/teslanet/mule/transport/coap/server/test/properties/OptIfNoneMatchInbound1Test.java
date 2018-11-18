package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptIfNoneMatchInbound1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setIfNoneMatch( true );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.if_none_match";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Boolean( true );
    }

}