package nl.teslanet.mule.transport.coap.server.test.utils;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class Data
{
    /**
     * Size of large testcontent
     */
    private static final int LARGE_CONTENT_SIZE= 8192;

    /**
     * Read resource as string.
     *
     * @param filePath the file path
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readResourceAsString(String filePath) throws java.io.IOException {
        return IOUtils.resourceToString( filePath, Charset.defaultCharset() );
    }


    /**
     * Create large test content of standard size 
     * @return the test content
     */
    public static byte[] getLargeContent()
    {
        byte[] content= new byte [LARGE_CONTENT_SIZE];
        for ( int i= 0; i < LARGE_CONTENT_SIZE; i++ )
        {
            content[i]= (byte) ( i % ( Byte.MAX_VALUE + 1 ) );
        }
        return content;
    }

    /**
     * Validates the test content of standard size
     * @param content to validate
     * @return true when the content is as expected, otherwise false
     */
    public static boolean validateLargeContent( byte[] content )
    {
        for ( int i= 0; i < LARGE_CONTENT_SIZE; i++ )
        {
            if ( content[i] != (byte) ( i % ( Byte.MAX_VALUE + 1 ) ) )
            {
                return false;
            } ;
        }
        return true;
    }
}
