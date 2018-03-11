/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Public License - v 2.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.server.config;

import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;

import java.net.InetSocketAddress;
import java.util.List;

import org.eclipse.californium.core.coap.CoAP;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

@Configuration(friendlyName = "Configuration")
public class ServerConfig extends EndpointConfig
{
    @Configurable
    @Default( value= "false")
    @Placement(tab= "General", group= "Server")
    //@FriendlyName(value = false)
    private boolean secure= false;

    public InetSocketAddress getInetSocketAddress()
    {
        int port;
       
        if ( secure )
        {
            port = ( getBindToSecurePort() != null ? Integer.parseInt( getBindToSecurePort()) : CoAP.DEFAULT_COAP_SECURE_PORT );
        }
        else
        {
            port = ( getBindToPort() != null ? Integer.parseInt( getBindToPort()) : CoAP.DEFAULT_COAP_PORT );
        }
        if ( getBindToHost() != null )
        {
            
            return new InetSocketAddress( getBindToHost(), port );            
        }
        else
        {
            return new InetSocketAddress( port );            
        }
    }

    /**
     * @return the secure
     */
    public boolean isSecure()
    {
        return secure;
    }

    /**
     * @param secure the secure to set
     */
    public void setSecure( boolean secure )
    {
        this.secure= secure;
    }


}