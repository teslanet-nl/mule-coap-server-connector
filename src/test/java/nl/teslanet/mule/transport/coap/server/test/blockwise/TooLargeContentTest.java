package nl.teslanet.mule.transport.coap.server.test.blockwise;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.junit.BeforeClass;


public class TooLargeContentTest extends AbstractBlockwiseTest
{
    /**
     * Size of large testcontent
     */
    private final int CONTENT_SIZE= 16000;
    private final int CLIENT_MAX_CONTENT_SIZE= 16000;

    private static HashMap< Code, String > paths;
    

    @BeforeClass
    static public void setUpClass() throws Exception
    {
        paths= new HashMap< Code, String >();
        paths.put( Code.GET, "/service/get_me" );
        paths.put( Code.PUT, "/service/put_me" );
        paths.put( Code.POST, "/service/post_me" );
        paths.put( Code.DELETE, "/service/delete_me" );
    }

    @Override
    protected String getPath( Code call )
    {
        return paths.get( call );
    }
    
    @Override
    protected int getContentSize()
    {
        return CONTENT_SIZE;
    }

    @Override
    protected int getClientMaxContentSize()
    {
        return CLIENT_MAX_CONTENT_SIZE;
    }
    
    @Override
    protected void validateInboundResponse( Code call, CoapResponse response )
    {
        //TODO californium seems not to support inbound blockwise for GET
        if ( call == Code.GET ) return;
        //TODO californium seems not to support inbound blockwise for DELETE
        if ( call == Code.DELETE ) return;
        assertNotNull( "no response on: " + call, response );
        assertEquals( "response is not REQUEST_ENTITY_TOO_LARGE : " + call +" response: " + response.getCode() + " msg: " + response.getResponseText(), ResponseCode.REQUEST_ENTITY_TOO_LARGE, response.getCode() );
    }
 }