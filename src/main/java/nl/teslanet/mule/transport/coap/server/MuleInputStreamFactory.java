/*******************************************************************************
 * Copyright (c) 2019 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License - v 2.0 
 * which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.server;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.californium.elements.util.SslContextUtil.InputStreamFactory;
import org.mule.util.IOUtils;

/**
 * InputStreamFactory that creates input streams using Mule's IOUtils.
 *
 */
public class MuleInputStreamFactory implements InputStreamFactory
{
    /**
     * The scheme used in resource Uri's that are loaded using Mule's IOUtils.
     */
    protected static final String MULE_RESOURCE_SCHEME= "mule://";


    /* (non-Javadoc)
     * @see org.eclipse.californium.elements.util.SslContextUtil.InputStreamFactory#create(java.lang.String)
     */
    @Override
    public InputStream create( String uri ) throws IOException
    {
        String resource = uri.substring(MULE_RESOURCE_SCHEME.length());
        InputStream inStream= IOUtils.getResourceAsStream( resource, this.getClass(), true, true );
        if ( inStream == null ) throw new IOException( "resource not found: " + resource );
        return inStream;
    }
    
    public final String getScheme()
    {
        return MULE_RESOURCE_SCHEME;
    }
}
