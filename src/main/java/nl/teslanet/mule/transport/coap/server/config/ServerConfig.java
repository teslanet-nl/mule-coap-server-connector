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

/**
 * The CoAP server configuration contains attributes defining the CoAP endpoint and child elements that define CoAP 
 * resources that will be served. Add resources by adding "&lt;coap-server:resource&gt;" child-elements to the "&lt;coap-server:config&gt;" element. 
 * The resources can - in turn - contain other resources. This hierarchy of resources defines the CoAP ReST interface.  
 * Every resource element defines its name and the operations that are available on it. The operations on resources possible are Get, Post, Put, Delete and Observe. 
 * When the EarlyAck flag is set an acknowledgement is immediately sent back to the client, before processing the request and returning the response. 
 * Use this when processing takes longer than the acknowledgment-timeout of the client.
 * Clients can refer to resources by their path in the hierarchy e.g. "/root/child", where the resource named "root" contains the resource named "child".
 * The size and type attributes can be added to resources which will be reported to clients when they issue a Discovery request.
 * Example: <pre>
 * {@code
 *     <coap-server:config name="CoAP_Server_Configuration"
 *       doc:name="CoAP Server Connector: Configuration"
 *       notificationCheckIntervalTime="5000" 
 *        notificationCheckIntervalCount="5"
 *        keyStoreLocation="keyStore.jks" 
 *        keyStorePassword="endPass" 
 *        privateKeyAlias="server"
 *        trustStoreLocation="trustStore.jks" 
 *        trustStorePassword="rootPass"
 *        trustedRootCertificateAlias="root" 
 *        >
 *        
 *        <coap-server:resources>
 *            <coap-server:resource name="large" get="true" earlyAck="true" size="2345345" type="application/json"/>
 *            <coap-server:resource name="slow" get="true" earlyAck="true"/>
 *            <coap-server:resource name="alphabet" get="true" post="true" observe="true">
 *                <coap-server:resources>
 *                    <coap-server:resource name="a" get="true" post="true" put="true" delete="true"
 *                        observe="true" earlyAck="true" >
 *                        <coap-server:resources>
 *                            <coap-server:resource name="b" get="true"/>
 *                            <coap-server:resource name="c" get="true"/>
 *                        </coap-server:resources>
 *                    </coap-server:resource>
 *                </coap-server:resources>
 *            </coap-server:resource>
 *            <coap-server:resource name="hello" get="true" observe="true">
 *                <coap-server:resources>
 *                    <coap-server:resource name="there" get="true"/>
 *                    <coap-server:resource name="changeme" get="true" put="true" observe="true"/>
 *                    <coap-server:resource name="deleteme" get="true" delete="true"/>
 *                    <coap-server:resource name="collect" post="true" type="40"/>
 *                </coap-server:resources>
 *            </coap-server:resource>
 *        </coap-server:resources>
 *   </coap-server:config>
 *} </pre>
 */
@Configuration(friendlyName = "Server Configuration")
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