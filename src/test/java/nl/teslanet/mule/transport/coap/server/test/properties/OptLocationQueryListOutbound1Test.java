package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationQueryListOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_query.list";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getLocationQuery();
    }

    @Override
    protected Object getPropertyValue() throws InvalidETagException
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "first=1");
        list.add( "second=2");
        
        return list;
    }

    @Override
    protected Object getExpectedOptionValue() throws InvalidETagException
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "first=1");
        list.add( "second=2");
        
        return list;
    }

}