package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptEtagListInbound2Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        options.addETag( new ETag( "A0").asBytes() );         
        options.addETag( new ETag( "0011FF").asBytes() );         
        options.addETag( new ETag( "0011223344556677").asBytes() );         
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.etag.list";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        LinkedList<ETag> list= new LinkedList<ETag>();
        list.add( new ETag("A0") );
        list.add( new ETag("0011FF") );
        list.add( new ETag("0011223344556677") );
        
        return list;
    }
}