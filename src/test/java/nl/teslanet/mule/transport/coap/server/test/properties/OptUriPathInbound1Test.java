package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptUriPathInbound1Test extends AbstractInboundPropertyTestcase
{
 
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        // nop
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_path";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        return resourcePath.substring( 1 );
    }
}