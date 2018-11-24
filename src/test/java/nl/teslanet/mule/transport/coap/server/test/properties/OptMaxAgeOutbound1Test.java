package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptMaxAgeOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.max_age";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getMaxAge();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new Long( 120 );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        return new Long( 120 );
    }}