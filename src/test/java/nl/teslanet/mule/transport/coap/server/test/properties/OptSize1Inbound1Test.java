package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//TODO add observe functionality test
public class OptSize1Inbound1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setSize1( 512 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.size1";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 512 );
    }

}