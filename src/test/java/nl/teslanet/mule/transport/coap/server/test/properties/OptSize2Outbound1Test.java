package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptSize2Outbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.size2";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getSize2();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new Integer( 4890 );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        return new Integer( 4890 );
    }}