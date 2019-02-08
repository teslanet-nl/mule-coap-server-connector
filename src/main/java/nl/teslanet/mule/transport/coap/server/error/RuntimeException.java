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
 * Exception that occurs during processing requests
 *
 */
public class RuntimeException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID= 1L;

    public RuntimeException()
    {
        super();
    }

    public RuntimeException( String message )
    {
        super( message );
    }

    public RuntimeException( Throwable cause )
    {
        super( cause );
    }

    public RuntimeException( String message, Throwable cause )
    {
        super( message, cause );
    }

    public RuntimeException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

}
