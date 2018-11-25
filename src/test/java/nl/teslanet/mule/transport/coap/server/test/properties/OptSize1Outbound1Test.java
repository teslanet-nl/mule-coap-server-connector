package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptSize1Outbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.size1";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getSize1();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new Integer( 231 );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        return new Integer( 231 );
    }}