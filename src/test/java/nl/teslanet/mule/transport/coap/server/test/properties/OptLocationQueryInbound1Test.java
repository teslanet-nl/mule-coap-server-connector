package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationQueryInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        options.setLocationQuery( "?first=1&second=2" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_query";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        //TODO also prepending ?
        return new String( "first=1&second=2" );
    }
}