package nl.teslanet.mule.transport.coap.server.test.properties;


public class Stringable
{
    private Object value;

    public Stringable( Object value )
    {
        this.value= value;
    }

    @Override
    public String toString()
    {
        return value.toString();
    }
}
