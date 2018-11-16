package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptContentFormatOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.content_format";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getContentFormat();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new Integer( 41 );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        return new Integer( 41 );
    }}