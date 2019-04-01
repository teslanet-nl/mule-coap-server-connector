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

import java.net.InetSocketAddress;

import org.eclipse.californium.core.coap.CoAP;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Default;

/**
 * The CoAP server configuration contains attributes defining the CoAP endpoint and child elements that define CoAP 
 * resources that will be served. Add resources by adding "&lt;coap-server:resource&gt;" child-elements to the "&lt;coap-server:config&gt;" element. 
 * The resources can - in turn - contain other resources. This hierarchy of resources defines the CoAP ReST interface.  
 * Every resource element defines its name and the operations that are available on it. The operations on resources possible are Get, Post, Put, Delete and Observe. 
 * When the EarlyAck flag is set an acknowledgement is immediately sent back to the client, before processing the request and returning the response. 
 * Use this when processing takes longer than the acknowledgment-timeout of the client.
 * Clients can refer to resources by their path in the hierarchy e.g. "/root/child", where the resource named "root" contains the resource named "child".
 * The size and type attributes can be added to resources which will be reported to clients when they issue a Discovery request.
 */
@Configuration(friendlyName = "Server Configuration")
public class ServerConfig extends EndpointConfig
{
    /**
     * When true DTLS is used to secure CoAP communication.  
     * Key and certificate must then be configured.
     */
    @Configurable
    @Default( value= "false")
    @Placement(tab= "General", group= "Server")
    //@FriendlyName(value = false)
    private boolean secure= false;

    /**
     * When true each CoAP message will be logged.
     */
    @Configurable
    @Default( value= "false")
    @Placement(tab= "Logging", group= "CoAP")
    //@FriendlyName(value = false)
    private boolean logMessages= false;
    
    /**
     * Gets the Socket address the server listens on
     */
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

    /**
     * Indicates whether CoAP messages should be logged
     * @return the logMessages
     */
    public boolean isLogMessages()
    {
        return logMessages;
    }

    /**
     * Set flag that indicates whether CoAP messages should be logged
     * @param logMessages the logMessages to set
     */
    public void setLogMessages( boolean logMessages )
    {
        this.logMessages= logMessages;
    }


}