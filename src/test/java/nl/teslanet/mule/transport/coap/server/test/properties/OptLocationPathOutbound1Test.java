package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;


public class OptLocationPathOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_path";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getLocationPath();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new String( "/test/this/path" );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "test");
        list.add( "this");
        list.add( "path");
        
        return list;
    }}