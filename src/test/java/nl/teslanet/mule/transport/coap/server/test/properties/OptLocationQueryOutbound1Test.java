package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;


public class OptLocationQueryOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_query";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getLocationQuery();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new String( "?first=1&second=2" );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "first=1");
        list.add( "second=2");
        
        return list;
    }}