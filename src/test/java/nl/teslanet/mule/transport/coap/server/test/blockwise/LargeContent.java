package nl.teslanet.mule.transport.coap.server.test.blockwise;


public class LargeContent
{
    private static final int SIZE= 8192;

    private LargeContent()
    {
        super();
    }

    public static byte[] get()
    {
        byte[] content= new byte [SIZE];
        for ( int i= 0; i < SIZE; i++ )
        {
            content[i]= (byte) ( i % ( Byte.MAX_VALUE + 1 ) );
        }
        return content;
    }

    public static boolean validate( byte[] content )
    {
        for ( int i= 0; i < SIZE; i++ )
        {
            if ( content[i] != (byte) ( i % ( Byte.MAX_VALUE + 1 ) ) )
            {
                return false;
            } ;
        }
        return true;
    }
}
