package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptEtagListOutbound1Test2 extends AbstractOutboundPropertyTestcase
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
        return new ETag( "0011FF" );
    }

    @Override
    protected Object getExpectedOptionValue() throws InvalidETagException
    {
        LinkedList< byte[] > list= new LinkedList< byte[] >();
        list.add( new ETag( "0011FF" ).asBytes() );

        return list;
    }

    @Override
    protected boolean optionValueIsCollectionOfByteArray()
    {
        return true;
    }
}