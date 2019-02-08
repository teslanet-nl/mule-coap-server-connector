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
package nl.teslanet.mule.transport.coap.server.error;


/**
 * The ResourceUriException is thrown when the CoAP Resource URI is invalid.
 * 
 */
public class ResourceUriException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID= 1L;

    public ResourceUriException( String Uri )
    {
        super("Invalid CoAP resource uri { " + Uri + " } ");
    }

    public ResourceUriException( String Uri, String message )
    {
        super( "Invalid CoAP resource uri { " + Uri + " } " + message );
    }

    public ResourceUriException( String Uri, Throwable cause )
    {
        super( "Invalid CoAP resource uri { " + Uri + " } ", cause );
    }

    public ResourceUriException( String Uri, String message, Throwable cause )
    {
        super( "Invalid CoAP resource uri { " + Uri + " } " + message, cause );
    }

    public ResourceUriException( String Uri, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( "Invalid CoAP resource uri { " + Uri + " } " + message, cause, enableSuppression, writableStackTrace );
    }
}
