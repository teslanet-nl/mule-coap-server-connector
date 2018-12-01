package nl.teslanet.mule.transport.coap.server.test.properties;


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.CoAP.Code;

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptUriPathListInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected String getConfigResources()
    {
        return "mule-config/properties/testserver2.xml";
    };
    
    @Override
    protected String getPath( Code call )
    {
        return "/service/call_me";
    }
    
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
        LinkedList<String> list= new LinkedList<String>();
        list.add( "service");
        list.add( "call_me");
        
        return list;
    }
}