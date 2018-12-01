package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO add observe functionality test
public class OptSize2Inbound1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setSize2( 456 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.size2";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 456 );
    }

}