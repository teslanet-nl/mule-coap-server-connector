package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptContentFormatInboundTest extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options )
    {
        options.setContentFormat( 41 );        
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.content_format";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 41 );
    }
}