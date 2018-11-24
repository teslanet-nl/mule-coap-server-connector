package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationPathListOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_path.list";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getLocationPath();
    }

    @Override
    protected Object getPropertyValue() throws InvalidETagException
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "test");
        list.add( "this");
        list.add( "path");
        
        return list;
    }

    @Override
    protected Object getExpectedOptionValue() throws InvalidETagException
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "test");
        list.add( "this");
        list.add( "path");
        
        return list;
    }

}