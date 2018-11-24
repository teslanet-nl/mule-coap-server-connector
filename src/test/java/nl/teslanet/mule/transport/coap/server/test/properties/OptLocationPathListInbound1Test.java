package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationPathListInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        options.setLocationPath( "/test/this/path" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_path.list";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        LinkedList<String> list= new LinkedList<String>();
        list.add( "test");
        list.add( "this");
        list.add( "path");
        
        return list;
    }
}