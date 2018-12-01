package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionSet;
import org.junit.Before;

public class OptOtherOptionNoCacheKeyInbound4Test extends AbstractInboundPropertyTestcase
{
    private Option option;
    
    @Before
    public void initializeOption()
    {
        byte[] value= { (byte)0x12, (byte) 0xFF, (byte)0x45 };
        option= new Option();
        option.setNumber( 65308 );
        option.setValue( value );
    }

    @Override
    protected void addOption( OptionSet options )
    {
        options.addOption( option );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.other.65308.no_cache_key";
    }

  
    @Override
    protected Object getExpectedPropertyValue()
    {
        return Boolean.TRUE;

    }

}