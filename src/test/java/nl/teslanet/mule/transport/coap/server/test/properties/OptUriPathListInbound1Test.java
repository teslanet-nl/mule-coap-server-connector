package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptUriPathListInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        //nop
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_path.list";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        String[] resources= resourcePath.substring( 1 ).split( "/");
        
        LinkedList<String> list= new LinkedList<String>();
        for ( String resource : resources)
        {
            list.add( resource );
        }        
        return list;
    }
}