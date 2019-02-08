/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.server;


import org.mule.api.callback.SourceCallback;

import nl.teslanet.mule.transport.coap.commons.Defs;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;


public class Listener
{

    String uri;

    SourceCallback callback;

    public Listener( String uri, SourceCallback callback ) throws ResourceUriException 
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
     * @throws ResourceUriException 
     */
    //TODO: make URIpattern class
    public void setUri( String uri ) throws ResourceUriException  
    {
        //TODO assure no bad chars
        if ( uri == null ) throw new ResourceUriException( "null", ", null is not allowed.");
        this.uri= uri.trim();
        if ( !this.uri.startsWith( Defs.COAP_URI_PATHSEP ))
        {
            this.uri= Defs.COAP_URI_PATHSEP + this.uri; 
        };
        int wildcardIndex= this.uri.indexOf( Defs.COAP_URI_WILDCARD );
        if ( wildcardIndex >= 0 && wildcardIndex < this.uri.length()-1) throw new ResourceUriException( uri, ", wildcard needs to be last character.");
        if ( this.uri.length() < 2 ) throw new ResourceUriException( uri, ", uri cannot be empty."); 

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
