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

package nl.teslanet.mule.transport.coap.server;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.List;

import javax.inject.Inject;
import javax.resource.spi.work.WorkException;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.interceptors.MessageTracer;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.util.IOUtils;

import nl.teslanet.mule.transport.coap.server.config.ResourceConfig;
import nl.teslanet.mule.transport.coap.server.config.ServerConfig;
import nl.teslanet.mule.transport.coap.server.error.EndpointConstructionException;
import nl.teslanet.mule.transport.coap.server.error.ErrorHandler;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;


/**
 * Mule CoAP connector - CoapServer. 
 * The CoapServer Connector can be used in Mule applications to implement CoAP servers.
 * A CoAP server is defined by means of a set resources on which requests can be done like GET, POST, PUT etc. .
 * The server CoAP endpoint has a number of configuration parameters that can be used to tune behavior of the server. 
 * These parameters have sensible defaults and need only to be set for specific needs.     
 */
@Connector(
    name= "coap-server", 
    friendlyName= "CoAP Server", 
    schemaVersion= "2.0",
    minMuleVersion="3.8.0",
    //namespace= "http://www.mulesoft.org/schema/mule/coap-server",
    schemaLocation= "http://www.teslanet.nl/schema/mule/coap-server/2.0/mule-coap-server.xsd"
)
@OnException(handler= ErrorHandler.class)
public class CoapServerConnector
{

    @Config
    @Placement(tab= "General", group= "Server", order= 1)
    private ServerConfig config;

    /**
     * List of Resources that will be served. 
     * The resources define their name and the operations that can be done on them by clients. These operations are Get, Post, Put, Delete and Observe. 
     * When the EarlyAck flag is set an acknowledgement is immediately sent back to the client and before processing the request. Use this when processing takes longer than the acknowledgment-timeout of the client.  
     */
    @Configurable
    @Optional
    @Placement(tab= "General", group= "Server", order= 2)
    //@FriendlyName(value = "Resources")
    //mule devkit doesnt allow this to be configured in ServerConfig
    private List< ResourceConfig > resources= null;

    private CoapServer server= null;

    private ResourceRegistry registry= null;

    @Inject
    private MuleContext context;

    @Start
    public void startServer() throws ConnectionException, WorkException
    {
        //TODO allow empty server
        if ( getResources() == null || getResources().isEmpty() )
        {
            throw new ConnectionException( ConnectionExceptionCode.UNKNOWN, "coap no resources configured", null );
        }

        // binds on UDP port 5683
        server= new CoapServer( NetworkConfig.createStandardWithoutFile() );
        registry= new ResourceRegistry( server.getRoot() );

        try
        {
            addEndPoint( server, config );
            addResources( server, getResources() );
        }
        catch ( Exception e )
        {
            throw new ConnectionException( null, null, "CoAP configuration error", e );
        }

        if ( config.isLogCoapMessages() )
        {
            // add special interceptor for message traces
            for ( Endpoint ep : server.getEndpoints() )
            {
                ep.addInterceptor( new MessageTracer() );
            }
        }
        server.start();
    }

    //    private void addEndPoints( CoapServer server, List< EndpointConfig > endpoints )
    //    {
    //        for ( EndpointConfig endpoint : endpoints )
    //        {
    //            server.addEndpoint( new CoapEndpoint( endpoint.getInetSocketAddress(), endpoint.getNetworkConfig() ) );
    //        }
    //    }

    private void addEndPoint( CoapServer server, ServerConfig config ) throws EndpointConstructionException
    {
        if ( !config.isSecure() )
        {
            CoapEndpoint.CoapEndpointBuilder builder= new CoapEndpoint.CoapEndpointBuilder();
            builder.setInetSocketAddress( config.getInetSocketAddress() );
            builder.setNetworkConfig( config.getNetworkConfig() );
            server.addEndpoint( builder.build() );
        }
        else
        {
            // Pre-shared secrets
            //TODO improve security (-> not in memory ) 
            InMemoryPskStore pskStore= new InMemoryPskStore();
            //pskStore.setKey("password", "sesame".getBytes()); // from ETSI Plugtest test spec

            // load the key store
            KeyStore keyStore;
            try
            {
                keyStore= KeyStore.getInstance( "JKS" );
            }
            catch ( KeyStoreException e1 )
            {
                throw new EndpointConstructionException( "cannot create JKS keystore instance", e1 );
            }
            InputStream in;
            try
            {
                in= IOUtils.getResourceAsStream( config.getKeyStoreLocation(), server.getClass(), true, true );
            }
            catch ( IOException e1 )
            {
                throw new EndpointConstructionException( "cannot load keystore from { " + config.getKeyStoreLocation() + " }", e1 );
            }
            try
            {
                keyStore.load( in, config.getKeyStorePassword().toCharArray() );
            }
            catch ( NoSuchAlgorithmException | CertificateException | IOException e1 )
            {
                throw new EndpointConstructionException( "cannot load keystore from { " + config.getKeyStoreLocation() + " } using passwd ***", e1 );
            }

            // load the trust store
            KeyStore trustStore;
            try
            {
                trustStore= KeyStore.getInstance( "JKS" );
            }
            catch ( KeyStoreException e1 )
            {
                throw new EndpointConstructionException( "cannot create JKS truststore instance", e1 );
            }
            try ( InputStream inTrust= IOUtils.getResourceAsStream( config.getTrustStoreLocation(), server.getClass(), true, true ); )

            {
                try
                {
                    trustStore.load( inTrust, config.getTrustStorePassword().toCharArray() );
                }
                catch ( NoSuchAlgorithmException | CertificateException | IOException e1 )
                {
                    throw new EndpointConstructionException( "cannot load truststore from { " + config.getTrustStoreLocation() + " } using passwd ***", e1 );
                }

                // You can load multiple certificates if needed
                DtlsConnectorConfig.Builder dtlsBuilder= new DtlsConnectorConfig.Builder();
                dtlsBuilder.setAddress( config.getInetSocketAddress() );
                dtlsBuilder.setPskStore( pskStore );
                try
                {
                    dtlsBuilder.setTrustStore( trustStore.getCertificateChain( config.getTrustedRootCertificateAlias() ) );
                }
                catch ( Exception e )
                {
                    throw new EndpointConstructionException( "certificate chain with alias { " + config.getTrustedRootCertificateAlias() + " } not found in truststore", e );
                }
                try
                {

                    dtlsBuilder.setIdentity(
                        (PrivateKey) keyStore.getKey( config.getPrivateKeyAlias(), config.getKeyStorePassword().toCharArray() ),
                        keyStore.getCertificateChain( config.getPrivateKeyAlias() ),
                        true );
                }
                catch ( Exception e )
                {
                    throw new EndpointConstructionException( "identity with private key alias { " + config.getPrivateKeyAlias() + " } could not be set" );
                }
                DTLSConnector dtlsConnector= new DTLSConnector( dtlsBuilder.build() );

                CoapEndpoint.CoapEndpointBuilder builder= new CoapEndpoint.CoapEndpointBuilder();
                builder.setNetworkConfig( config.getNetworkConfig() );
                builder.setConnector( dtlsConnector );
                server.addEndpoint( builder.build() );
            }
            catch ( IOException e1 )
            {
                throw new EndpointConstructionException( "cannot load truststore from { " + config.getTrustStoreLocation() + " }", e1 );
            }
        }

    }

    /**
     * Add all resources that are configured on the server 
     * @param server
     * @param resourceConfigs
     * @throws ResourceUriException 
     */
    private void addResources( CoapServer server, List< ResourceConfig > resourceConfigs ) throws ResourceUriException
    {
        if ( resourceConfigs == null )
        {
            //nothing to do
            return;
        }
        for ( ResourceConfig resourceConfig : resourceConfigs )
        {
            registry.add( null, resourceConfig );
        }
    }



    // A class with @Connector must contain exactly one method annotated with
    @Stop
    public void stopServer()
    {
        if ( server != null )
        {
            server.stop();
            server.destroy();
            server= null;
        }
        registry= null;
    }

    /**
     *  The Listen messageprocessor receives all incoming requests on the specified uri. 
     *  The uri needs to be an existing resource defined in de coap-server configuration. 
     *  Wildcards can also be used, like "/*" or "/some/deeper/resources/*". 
     *  When multiple listeners apply for a resource, the listener with the most specific uri will get the requests on it.
     *  CoAP options of the request are added to the inbound-scope of the MuleMessage. 
     *  <br/>Example: <br/>
     *  <code> 
     *    &lt;flow name="coap-serverFlow1"&gt;<br/>
     *    &emsp;&emsp;  &lt;coap-server:listen uri="/alphabet/*" config-ref="CoAP_Server_Configuration"/&gt;<br/>
     *    &emsp;&emsp;  &lt;set-variable variableName="method" value="#[ message.inboundProperties['coap.request.code']]"/&gt;<br/>
     *    &emsp;&emsp;  &lt;set-variable variableName="uri" value="#[message.inboundProperties['coap.request.uri'] ]"/&gt;<br/>
     *    &emsp;&emsp;  &lt;byte-array-to-string-transformer/&gt;<br/>
     *    &emsp;&emsp;  &lt;logger level="INFO" message="#[payload]"/&gt; <br/>
     *    &emsp;&emsp;  &lt;set-payload value="&amp;lt;my_response&amp;gt;Hi!&amp;lt;/my_response&amp;gt;" encoding="UTF-8" mimeType="application/xml"/&gt;<br/>
     *    &emsp;&emsp;  &lt;set-property propertyName="coap.response.code" value="CONTENT" /&gt;<br/>
     *    &lt;/flow&gt;<br/>
     *  </code>
     *  @param callback Set by Mule, not visible in xml.
     *  @param uri The uri (without scheme/host part) of the resource the listener get the requests to process. 
     *  @return The payload of the CoAP request.
     * @throws ResourceUriException thrown when uri is not valid
     */
    @Source( /* threadingModel=SourceThreadingModel.NONE */)
    public byte[] listen( SourceCallback callback, String uri ) throws ResourceUriException
    {
        registry.add( new Listener( uri, callback ) );
        return null;

    }

    /**
     *  The Resource Changed messageprocessor is used to trigger a notification to clients that observe the specified resource.  
     *  The uri needs to be an existing resource defined in the coap-server configuration or a dynamically created resource. 
     *  Wildcards can also be used, like "/*" or "/some/deeper/resources/*". 
     *  The observing clients are notified by issuing an internal get-request for every client that gets processed by the listener on the resource concerned.
     *  <br/>Example:<br/>
     *  <code> 
     *    &lt;coap-server:resource-changed config-ref="CoAP_Server_Configuration" uri="/hello/changeme"/&gt;<br/>
     *  </code>
     *  @param uri The uri (without scheme/host part) of the resource that has changed. 
     * @throws ResourceUriException thrown when resource uri is invalid.
     */
    @Processor
    public void resourceChanged( String uri ) throws ResourceUriException
    {
        if ( uri == null )
        {
            throw new ResourceUriException( "null" );
        }

        for ( ServedResource resource : registry.findResources( uri ) )
        {
            resource.changed();
        }
    }

    /**
     *  The Add Resource messageprocessor is used to dynamically add a resource to the CoAP server.
     *  The uri needs to be a complete resource-path, including all parent resource(s). 
     *  All parent resources in the path need to exist already.
     *  Requests on the resource can be done immediately, provided there is a listener configured for it, 
     *  e.g. by means of a listener that has a wildcard uri (for the example below a listener with uri="/alphabet/*" would do).
     *  <br/>Example:<br/>
     *  <code> 
     *    &lt;coap-server:add-resource config-ref="CoAP_Server_Configuration"  uri="/alphabet/z" get="true" delete="true"/&gt;
     *  </code>
     *  @param uri The uri (without scheme/host part) of the resource to add. 
     *  @param get When true the resource accepts get-requests. 
     *  @param put When true the resource accepts put-requests. 
     *  @param post When true the resource accepts post-requests. 
     *  @param delete When true the resource accepts delete-requests. 
     *  @param observable When true the resource accepts observe-requests. 
     *  @param earlyAck An immediate acknowledgement is sent tot the client before processing the request. 
     *  @param title The readable title of the resource. 
     *  @param ifdesc The interface ( if ) indicates interfaces-name the resource implements. 
     *  @param rt The defines the resource type. 
     *  @param ct The type of the resources content, specified as CoAP type number. 
     *  @throws ResourceUriException thrown when resource uri is not valid
     */
    @Processor
    public void addResource(
        String uri,
        @Default("false") boolean get,
        @Default("false") boolean put,
        @Default("false") boolean post,
        @Default("false") boolean delete,
        @Default("false") boolean observable,
        @Default("false") boolean earlyAck,
        @Optional String title,
        @FriendlyName("if") @Optional String ifdesc,
        @Optional String rt,
        @Optional String sz,
        @Optional String ct ) throws ResourceUriException
    {
        if ( uri == null )
        {
            throw new ResourceUriException( "null" );
        }
        String parentUri= ResourceRegistry.getParentUri( uri );
        String name= ResourceRegistry.getUriResourceName( uri );
        if ( name.length() <= 0 ) throw new ResourceUriException( "empty string" );

        ResourceConfig resourceConfig= new ResourceConfig();
        resourceConfig.setName( name );
        resourceConfig.setGet( get );
        resourceConfig.setPost( post );
        resourceConfig.setPut( put );
        resourceConfig.setDelete( delete );
        resourceConfig.setObservable( observable );
        resourceConfig.setEarlyAck( earlyAck );
        resourceConfig.setTitle( title );
        resourceConfig.setIfdesc( ifdesc );
        resourceConfig.setRt( rt );
        resourceConfig.setSz( sz );
        resourceConfig.setCt( ct );

        registry.add( parentUri, resourceConfig );
    }

    /**
     *  The Remove Resource  messageprocessor removes one or more resources from the server.  
     *  A wildcard can be used, e.g. "/tobefound/*". All resources that apply to the uri will be removed.
     *  Clients that observe a resource that is removed will be notified.
     *  <br/>Example:<br/>
     *  <code>
     *    &lt;coap-server:remove-resource config-ref="CoAP_Server_Configuration" uri="/alphabet/z" /&gt;
     *  </code><br/>
     *  @param uri The uri (without scheme/host part) of the resource(s) to be deleted. 
     * @throws ResourceUriException thrown when the uri is not valid.
     */
    @Processor
    public void removeResource( String uriPattern ) throws ResourceUriException
    {
        if ( uriPattern == null )
        {
            throw new ResourceUriException( "null" );
        }
            registry.remove( uriPattern );
    }

    /**
     *  The <code>&lt;coap-server:resource-exists/&gt;</code> messageprocessor returns 'true' when a resource on 
     *  the given uri is active, otherwise false. 
     *  A wildcard can be used, e.g. "/tobefound/*". When at least one resource is found that applies to the uri, the Boolean 'true' is returned. 
     *  @param uri The uri (without scheme/host part) of the resource(s) to be deleted. 
     *  @return true when at least one resource is found.
     * @throws ResourceUriException thrown when uri is not valid.
     */
    @Processor
    public Boolean ResourceExists( String uri ) throws ResourceUriException
    {
        if ( uri == null )
        {
            throw new ResourceUriException( "null" );
        }
        List< ServedResource > found= registry.findResources( uri );
        return !found.isEmpty();
    }

    //TODO add list-resources operation

    public ServerConfig getConfig()
    {
        return config;
    }

    public void setConfig( ServerConfig config )
    {
        this.config= config;
    }

    public void setContext( MuleContext context )
    {
        this.context= context;
    }

    /**
     * @return the Mule context
     */
    public MuleContext getContext()
    {
        return context;
    }

    public boolean isRootResource( Resource resource )
    {
        return server.getRoot().equals( resource );
    }

    public List< ResourceConfig > getResources()
    {
        return resources;
    }

    public void setResources( List< ResourceConfig > resources )
    {
        this.resources= resources;
    }

}