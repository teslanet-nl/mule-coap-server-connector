package nl.teslanet.mule.connectors.coap.server;


import org.mule.api.callback.SourceCallback;

import nl.teslanet.mule.connectors.coap.options.Defs;


public class Listener
{

    String uri;

    SourceCallback callback;

    public Listener( String uri, SourceCallback callback ) throws Exception
    {
        super();
        setUri( uri );
        setCallback( callback );
    }

    /**
     * @return the uri
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * @param uri the uri to set
     * @throws Exception 
     */
    public void setUri( String uri ) throws Exception
    {
        //TODO assure no bad chars
        if ( uri == null ) throw new Exception( "CoAP no uri specified on listener");
        this.uri= uri.trim();
        if ( !this.uri.startsWith( Defs.COAP_URI_PATHSEP ))
        {
            this.uri= Defs.COAP_URI_PATHSEP + this.uri; 
        };
        int wildcardIndex= this.uri.indexOf( Defs.COAP_URI_WILDCARD );
        if ( wildcardIndex >= 0 && wildcardIndex < this.uri.length()-1)throw new Exception( "CoAP wildcard in listener needs to be last char");
        if ( this.uri.length() < 2 ) throw new Exception( "CoAP listener uri cannot be empty.");

    }

    /**
     * @return the callback
     */
    public SourceCallback getCallback()
    {
        return callback;
    }

    /**
     * @param callback the callback to set
     */
    public void setCallback( SourceCallback callback )
    {
        //TODO assure not null
        this.callback= callback;
    }

}
