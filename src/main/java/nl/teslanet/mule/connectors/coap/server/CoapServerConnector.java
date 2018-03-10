package nl.teslanet.mule.connectors.coap.server;


import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
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
import org.eclipse.californium.scandium.config.DtlsConnectorConfig.Builder;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.callback.SourceCallback;
import org.mule.util.IOUtils;

import nl.teslanet.mule.connectors.coap.server.config.ResourceConfig;
import nl.teslanet.mule.connectors.coap.server.config.ServerConfig;
import nl.teslanet.mule.connectors.coap.server.error.ErrorHandler;


@Connector(name= "coap-server", friendlyName= "CoAP Server", schemaVersion= "1.0"
//namespace= "http://www.teslanet.nl/mule/connectors/coap/server",
//schemaLocation= "http://www.teslanet.nl/mule/connectors/coap/server/1.0/mule-coap-server.xsd"
)
@OnException(handler= ErrorHandler.class)
public class CoapServerConnector
{

    @Config
    @Placement(tab= "General", group= "Server", order= 1)
    private ServerConfig config;

    @Configurable
    @Optional
    @Placement(tab= "General", group= "Server", order= 2)
    //@FriendlyName(value = "Resources")
    private List< ResourceConfig > resources= null;

    //    @Configurable
    //    @Optional
    //    @Placement(tab= "Advanced", group= "Advanced")
    //    //@FriendlyName(value = "Endpoints")
    //    private List< EndpointConfig > endpoints= null;

    private CoapServer server= null;

    //private HashMap< String, ServedResource > servedResources;
    private ResourceRegistry registry= null;

    @Inject
    private MuleContext context;

    @Start
    public void startServer() throws ConnectionException, WorkException
    {

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

        //System.getSecurityManager().checkAccept( "localhost", 5683 );
        //server.addEndpoint(new CoapEndpoint(new InetSocketAddress("0.0.0.0", 5683)));
        //TODO make configurable
        // add special interceptor for message traces
        for ( Endpoint ep : server.getEndpoints() )
        {
            ep.addInterceptor( new MessageTracer() );
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

    private void addEndPoint( CoapServer server, ServerConfig config ) throws Exception
    {
        if ( !config.isSecure() )
        {
            server.addEndpoint( new CoapEndpoint( config.getInetSocketAddress(), config.getNetworkConfig() ) );
        }
        else
        {
            // Pre-shared secrets
            //TODO improve security (-> not in memory ) 
            InMemoryPskStore pskStore= new InMemoryPskStore();
            //pskStore.setKey("password", "sesame".getBytes()); // from ETSI Plugtest test spec

            // load the key store
            KeyStore keyStore= KeyStore.getInstance( "JKS" );
            //TODO load from from Mule util
            InputStream in= IOUtils.getResourceAsStream( config.getKeyStoreLocation(), server.getClass(), true, true );
            keyStore.load( in, config.getKeyStorePassword().toCharArray() );

            // load the trust store
            KeyStore trustStore= KeyStore.getInstance( "JKS" );
            //TODO load from from Mule util
            InputStream inTrust= IOUtils.getResourceAsStream( config.getTrustStoreLocation(), server.getClass(), true, true );
            trustStore.load( inTrust, config.getTrustStorePassword().toCharArray() );

            // You can load multiple certificates if needed
            DtlsConnectorConfig.Builder configBuider= new Builder( config.getInetSocketAddress() );
            configBuider.setPskStore( pskStore );
            try
            {
                configBuider.setTrustStore( trustStore.getCertificateChain( config.getTrustedRootCertificateAlias() ) );
            }
            catch ( Exception e )
            {
                throw new Exception( "coap: certificate chain with alias not found in truststore" );
            }
            try
            {

                configBuider.setIdentity(
                    (PrivateKey) keyStore.getKey( config.getPrivateKeyAlias(), config.getKeyStorePassword().toCharArray() ),
                    keyStore.getCertificateChain( config.getPrivateKeyAlias() ),
                    true );
            }
            catch ( Exception e )
            {
                throw new Exception( "coap: private key with alias not found in keystore" );
            }
            DTLSConnector connector= new DTLSConnector( configBuider.build() );
            //DTLSConnector connector = new DTLSConnector(new InetSocketAddress(DTLS_PORT), trustedCertificates);
            //connector.getConfig().setPrivateKey((PrivateKey)keyStore.getKey("server", KEY_STORE_PASSWORD.toCharArray()), keyStore.getCertificateChain("server"), true);

            server.addEndpoint( new CoapEndpoint( connector, config.getNetworkConfig() ) );
        }

    }

    private void addResources( CoapServer server, List< ResourceConfig > resourceConfigs ) throws Exception
    {
        for ( ResourceConfig resourceConfig : resourceConfigs )
        {
            ServedResource toServe= new ServedResource( this, resourceConfig );
            registry.add( null, toServe );
            addChildren( toServe );
        }
    }

    private void addChildren( ServedResource parent ) throws Exception
    {
        for ( ResourceConfig childResourceConfig : parent.getConfiguredResource().getResourceCollection() )
        {
            ServedResource childToServe= new ServedResource( this, childResourceConfig );

            registry.add( parent, childToServe );
            addChildren( childToServe );
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
     *  Register Listener
     *
     *  @param callback The sourcecallback used to dispatch message to the flow
     *  @throws Exception error produced while processing the payload
     */
    @Source( /* threadingModel=SourceThreadingModel.NONE */)
    public void listen( SourceCallback callback, String uri ) throws Exception
    {
        registry.add( new Listener( uri, callback ) );

    }

    @Processor
    public void resourceChanged( String uri ) throws Exception
    {
        if ( uri == null )
        {
            throw new Exception( "CoAP URI cannot be null." );
        }

        for ( ServedResource resource : registry.findResources( uri ) )
        {
            resource.changed();
        }
    }

    @Processor
    public void addResource(
        String uri,
        @Default("false") boolean get,
        @Default("false") boolean put,
        @Default("false") boolean post,
        @Default("false") boolean delete,
        @Default("false") boolean observe,
        @Default("false") boolean earlyAck ) throws Exception
    {
        if ( uri == null )
        {
            throw new Exception( "CoAP URI cannot be null." );
        }
        String parentUri= ResourceRegistry.getParentUri( uri );
        ServedResource parent= null;
        String name= ResourceRegistry.getUriResourceName( uri );
        if ( name.length() <= 0 ) throw new Exception( "CoAP resource name is empty" );

        ResourceConfig resourceConfig= new ResourceConfig();
        resourceConfig.setName( name );
        resourceConfig.setGet( get );
        resourceConfig.setPost( post );
        resourceConfig.setPut( put );
        resourceConfig.setDelete( delete );
        resourceConfig.setObserve( observe );
        resourceConfig.setEarlyAck( earlyAck );

        ServedResource toServe= new ServedResource( this, resourceConfig );
        parent= registry.getResource( parentUri );
        registry.add( parent, toServe );

    }

    @Processor
    public void deleteResource( String uri ) throws Exception
    {
        if ( uri == null )
        {
            throw new Exception( "CoAP URI cannot be null." );
        }

        for ( ServedResource resource : registry.findResources( uri ) )
        {
            registry.remove( resource );
        }
    }

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