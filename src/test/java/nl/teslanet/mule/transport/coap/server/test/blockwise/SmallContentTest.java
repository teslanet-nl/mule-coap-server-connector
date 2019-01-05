package nl.teslanet.mule.transport.coap.server.test.blockwise;


import java.util.HashMap;

import org.eclipse.californium.core.coap.CoAP.Code;
import org.junit.BeforeClass;


public class SmallContentTest extends AbstractBlockwiseTest
{
    /**
     * Size of large testcontent
     */
    private final int CONTENT_SIZE= 10;
    private final int CLIENT_MAX_CONTENT_SIZE= 8192;

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
 }