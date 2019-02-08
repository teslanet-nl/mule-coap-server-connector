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


import java.util.logging.Level;
import java.util.logging.Logger;

import org.mule.api.annotations.Handle;
import org.mule.api.annotations.components.Handler;


@Handler
public class ErrorHandler
{

    /** The logger. */
    private static final Logger LOGGER= Logger.getLogger( ErrorHandler.class.getCanonicalName() );

    @Handle
    public void handle( Exception ex ) throws Exception
    {
        LOGGER.log( Level.SEVERE, ex.getMessage(), ex );

    }
}