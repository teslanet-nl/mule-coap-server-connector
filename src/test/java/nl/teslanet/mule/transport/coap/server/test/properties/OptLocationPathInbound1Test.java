package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationPathInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        options.setLocationPath( "/test/this/path" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_path";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        //TODO make this coherent with other paths with prepending / => /test/this/path
        return new String( "test/this/path" );
    }
}