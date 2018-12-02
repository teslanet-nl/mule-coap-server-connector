package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationQueryListInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        options.setLocationQuery( "?first=1&second=2" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_query.list";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "first=1");
        list.add( "second=2");
        
        return list;
    }
}