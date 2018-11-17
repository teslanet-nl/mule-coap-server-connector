package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptEtagListOutbound3mTest extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.etag.list";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getETags();
    }

    @Override
    protected Object getPropertyValue() throws InvalidETagException
    {
        LinkedList< String > list= new LinkedList< String >();
        list.add( new String( "hello" ) );
        list.add( new String( "olla" ) );
        list.add( new String( "hoi" ) );

        return list;
    }

    @Override
    protected Object getExpectedOptionValue() throws InvalidETagException
    {
        LinkedList< byte[] > list= new LinkedList< byte[] >();
        list.add( new ETag( "68656C6C6F" ).asBytes() );
        list.add( new ETag( "6F6C6C61" ).asBytes() );
        list.add( new ETag( "686F69" ).asBytes() );

        return list;
    }

    @Override
    protected boolean optionValueIsCollectionOfByteArray()
    {
        return true;
    }
}