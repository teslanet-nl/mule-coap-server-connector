package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptIfNoneMatchInbound3Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        //options.setIfNoneMatch( false );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.if_none_match";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Boolean( false );
    }

}