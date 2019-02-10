/*******************************************************************************
 * Copyright (c) 2017, 2018, 2019 (teslanet.nl) Rogier Cobben.
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


import java.security.cert.Certificate;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.network.interceptors.MessageTracer;
import org.eclipse.californium.core.server.resources.Resource;
import org.eclipse.californium.elements.util.SslContextUtil;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.CertificateType;
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

@Connector(name= "coap-server", friendlyName= "CoAP Server", schemaVersion= "2.0", minMuleVersion= "3.8.0",
        //namespace= "http://www.mulesoft.org/schema/mule/coap-server",
        schemaLocation= "http://www.teslanet.nl/schema/mule/coap-server/2.0/mule-coap-server.xsd")
@OnException(handler= ErrorHandler.class)
public class CoapServerConnector
{
    /**
     * The configuration that is used to construct the CoAP Server instance.
     */
    @Config
    @Placement(tab= "General", group= "Server", order= 1)
    private ServerConfig config;

    /**
     * List of Resources that will be served. 
     * The resources define their name and the operations that are available to clients. 
     * Resources can have child resources, forming a tree of resources.
    */
    @Configurable
    @Optional
    @Placement(tab= "General", group= "Server", order= 2)
    //@FriendlyName(value = "Resources")
    //mule devkit doesnt allow this to be configured in ServerConfig
    private List< ResourceConfig > resources= null;

    /**
     * The CoAP server instance
     */
    private CoapServer server= null;

    /**
     * The registry of the resources served
     */
    private ResourceRegistry registry= null;

    /**
     * The context that is injected by Mule.
     */
    @Inject
    private MuleContext context;

    /**
     * Start the CoAP server. Called by Mule on application start.
     * @throws ConnectionException thrown when the CoAP server could not start.
     */
    @Start
    public void startServer() throws ConnectionException
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

    /**
     * Add a new CoAP endpoint to the server.
     * @param server that will use the endpoint.
     * @param config the configuration containing the endpoint parameters.
     * @throws EndpointConstructionException
     */
    private void addEndPoint( CoapServer server, ServerConfig config ) throws EndpointConstructionException
    {
        if ( !config.isSecure() )
        {
            CoapEndpoint.Builder builder= new CoapEndpoint.Builder();
            builder.setInetSocketAddress( config.getInetSocketAddress() );
            builder.setNetworkConfig( config.getNetworkConfig() );
            server.addEndpoint( builder.build() );
        }
        else
        {
            //TODO: add allowing all clients to connect
            MuleInputStreamFactory streamFactory= new MuleInputStreamFactory();
            SslContextUtil.configure( streamFactory.getScheme(), streamFactory );
            // Pre-shared secrets
            //TODO improve security (-> not in memory ) 
            InMemoryPskStore pskStore= new InMemoryPskStore();
            // put in the PSK store the default identity/psk for tinydtls tests
            //pskStore.setKey( "Client_identity", "secretPSK".getBytes() );
            try
            {
                // load the key store
                SslContextUtil.Credentials serverCredentials= SslContextUtil.loadCredentials(
                    streamFactory.getScheme() + config.getKeyStoreLocation(),
                    config.getPrivateKeyAlias(),
                    ( config.getKeyStorePassword() != null ? config.getKeyStorePassword().toCharArray() : null ),
                    ( config.getPrivateKeyPassword() != null ? config.getPrivateKeyPassword().toCharArray() : null ) );
                Certificate[] trustedCertificates= SslContextUtil.loadTrustedCertificates(
                    streamFactory.getScheme() + config.getTrustStoreLocation(),
                    config.getTrustedRootCertificateAlias(),
                    ( config.getTrustStorePassword() != null ? config.getTrustStorePassword().toCharArray() : null ) );

                DtlsConnectorConfig.Builder builder= new DtlsConnectorConfig.Builder();
                builder.setAddress( config.getInetSocketAddress() );
                builder.setPskStore( pskStore );
                builder.setIdentity( serverCredentials.getPrivateKey(), serverCredentials.getCertificateChain(), CertificateType.RAW_PUBLIC_KEY, CertificateType.X_509 );
                builder.setTrustStore( trustedCertificates );
                builder.setRpkTrustAll();
                DTLSConnector dtlsConnector= new DTLSConnector( builder.build() );
                CoapEndpoint.Builder endpointBuilder= new CoapEndpoint.Builder();
                endpointBuilder.setNetworkConfig( config.getNetworkConfig() );
                endpointBuilder.setConnector( dtlsConnector );
                server.addEndpoint( endpointBuilder.build() );
            }
            catch ( Exception e )
            {
                throw new EndpointConstructionException( "cannot construct secure endpoint", e );
            }
        }
    }

    /**
     * Construct resources and add them to the server.
     * @param server the server to add the resources to.
     * @param resourceConfigs the configuration containing the resource parameters. 
     * @throws ResourceUriException thrown when Resource construction failed.  
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

    /**
     * Stop the CoAP server. Called by Mule when the application is stopped.
     */
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

    //TODO select method to listen for
    //TODO uriPattern
    /**
     * The Listen messageprocessor receives all incoming requests on resources that comply to the specified uri. 
     * Wildcards can also be used, like "/*" or "/some/deeper/resources/*". 
     * When multiple listeners apply for a resource, the listener with the most specific uri will get the requests on it.
     * CoAP options of the request are added to the inbound scope of the MuleMessage. 
     * The result of the flow is returned as CoAP response. 
     * CoAP options in the outbound property scope are put in the CoAP response.  
     * @param uri The uri pattern of the resources for which the listener will receive requests to process. 
     * @return The payload of the CoAP request.
     * @throws ResourceUriException thrown when uri is not valid
     */
    @Source
    public byte[] listen( SourceCallback callback, String uri ) throws ResourceUriException
    {
        registry.add( new Listener( uri, callback ) );
        return null;

    }

    //TODO uriPattern
    /**
     * The Resource Changed messageprocessor indicates the state of resources has changed and 
     * observing clients need to be notified. For every observing client and resource an internal get-request
     * is issued on the listener for the resource concerned.
     * The observing clients are notified by issuing an internal get-request for every client that gets processed by the listener on the resource concerned.
     * @param uri The uri pattern that specify the resource(s) that have changed. Wildcards can be used, like "/*" or "/some/deeper/resources/*". 
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
     *  The resource is available to clients immediately, provided there is a listener configured that 
     *  has an uri pattern that applies.
     *  @param uri The uri of the resource to add. 
     *  @param get When true the resource accepts get-requests. 
     *  @param put When true the resource accepts put-requests. 
     *  @param post When true the resource accepts post-requests. 
     *  @param delete When true the resource accepts delete-requests. 
     *  @param observable When true the resource accepts observe-requests. 
     *  @param earlyAck An immediate acknowledgement is sent to the client before processing the request and returning response. 
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

    //TODO add notification parameter
    //TODO uriPattern
    /**
     * The Remove Resource  messageprocessor removes one or more resources from the server.  
     * All resources that apply to the uri will be removed.
     * Clients that observe a resource that is removed will be notified.
     * @param uri The uri of the resource(s) to be deleted. Wildcards can be used, like "/*" or "/some/deeper/resources/*". 
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
     *  The Resource Exists messageprocessor returns 'true' when a resource on 
     *  the given uri is active on the server, otherwise false. 
     *  When at least one resource is found that applies to the uri, the Boolean 'true' is returned. 
     *  @param uri The uri pattern of the resource(s) to be found. A wildcard can be used, e.g. "/tobefound/*". 
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

    //TODO add method, usermanual item, schema update, unit test
    //TODO uriPattern
    /**
     *  The List Resources messageprocessor returns a list of uri's of 
     *  the resources that match given uri pattern. 
     *  @param uri The uri pattern of the resource(s) to be found. A wildcard can be used, e.g. "/tobefound/*". 
     *  @return The list of resource uri's.
     * @throws ResourceUriException thrown when uri is not valid.
     */
    //    @Processor
    //    public List< String > ListResources( String uri ) throws ResourceUriException
    //    {
    //        if ( uri == null )
    //        {
    //            throw new ResourceUriException( "null" );
    //        }
    //        List< String > uriList= new ArrayList< String >();
    //        for ( ServedResource found : registry.findResources( uri ) )
    //        {
    //            uriList.add( found.getURI() );
    //        }
    //        return new CopyOnWriteArrayList< String >( uriList );
    //    }

    /**
     * Get the configuration
     * @return the configuration object
     */
    public ServerConfig getConfig()
    {
        return config;
    }

    /**
     * Set the configuration object
     * @param config the configuration object
     */
    public void setConfig( ServerConfig config )
    {
        this.config= config;
    }

    /**
     * Set the Mule context
     * @param context the Mule context
     */
    public void setContext( MuleContext context )
    {
        this.context= context;
    }

    /**
     * Get the Mule context
     * @return the Mule context
     */
    public MuleContext getContext()
    {
        return context;
    }

    /**
     * Test whether a resource is the root resource.
     * @param resource that is tested
     * @return true when given resource is the root resource, otherwise false
     */
    public boolean isRootResource( Resource resource )
    {
        return server.getRoot().equals( resource );
    }

    /**
     * Get configured resources  
     * @return the list resource configurations
     */
    public List< ResourceConfig > getResources()
    {
        return resources;
    }

    /**
     * Set configured resources 
     * @param resources the resource configs to set.
     */
    public void setResources( List< ResourceConfig > resources )
    {
        this.resources= resources;
    }

}