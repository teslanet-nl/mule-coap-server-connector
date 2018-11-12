package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;


public class OptAcceptInboundTest extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setAccept( 41 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.accept";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 41 );
    }

}