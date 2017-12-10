package nl.teslanet.mule.connectors.coap.server;


import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.resource.spi.work.WorkException;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.Resource;
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

import nl.teslanet.mule.connectors.coap.server.config.ResourceConfig;
import nl.teslanet.mule.connectors.coap.server.config.ServerConfig;
import nl.teslanet.mule.connectors.coap.server.error.ErrorHandler;


@Connector(name= "coap-server", friendlyName= "CoAP Server Connector", schemaVersion= "1.0"
//namespace= "http://www.teslanet.nl/mule/connectors/coap/server",
//schemaLocation= "http://www.teslanet.nl/mule/connectors/coap/server/1.0/mule-coap-server.xsd"
)
@OnException(handler= ErrorHandler.class)
public class CoapServerConnector
{

    @Config
    @Placement(group= "Server")
    private ServerConfig config;

    @Configurable
    @Optional
    @Placement(group= "Server")
    //@FriendlyName(value = "Resources")
    private List< ResourceConfig > resourceConfigs;

    public List< ResourceConfig > getResourceConfigs()
    {
        return resourceConfigs;
    }

    public void setResourceConfigs( List< ResourceConfig > resourceConfigs )
    {
        this.resourceConfigs= resourceConfigs;
    }

    private CoapServer server= null;

    //private HashMap< String, ServedResource > servedResources;
    private ResourceRegistry registry= null;

    @Inject
    private MuleContext context;

    private NetworkConfig networkConfig;

    @Start
    public void startServer() throws ConnectionException, WorkException
    {
        
        if ( getResourceConfigs() == null || getResourceConfigs().isEmpty() )
        {
            throw new ConnectionException( ConnectionExceptionCode.UNKNOWN, "coap resources not defined", null );
        }

        networkConfig= NetworkConfig.createStandardWithoutFile();
        networkConfig.setLong( NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME, 60 * 1000 ); // ms., value )
        // binds on UDP port 5683
        server= new CoapServer( networkConfig );
        registry= new ResourceRegistry( server.getRoot());

        try
        {
            addResources( server, getResourceConfigs() );
        }
        catch ( Exception e )
        {
            throw new ConnectionException( null, null, "CoAP configuration error", e );
        }

        //System.getSecurityManager().checkAccept( "localhost", 5683 );
        //server.addEndpoint(new CoapEndpoint(new InetSocketAddress("0.0.0.0", 5683)));
        server.start();
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
            //servedResources.put( childToServe.getURI(), childToServe );
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
        }
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
        registry.add(  new Listener( uri, callback ) );

    }

    @Processor
    public void resourceChanged( String uri ) throws Exception
    {
        if ( uri == null )
        {
            throw new Exception( "CoAP URI cannot be null." );
        }
        
        for ( ServedResource resource : registry.findResources( uri ))
        {
            resource.changed();
        }
    }

    @Processor
    public void addResource( String uri, @Default( "false" ) Boolean get, @Default( "false" ) Boolean put, @Default( "false" ) boolean post, @Default( "false" ) boolean delete, @Default( "false" ) boolean observable, @Default( "false" ) boolean delayedResponse ) throws Exception
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
        resourceConfig.setDelete( put );
        resourceConfig.setObservable( observable );
        resourceConfig.setDelayedResponse( delayedResponse );
 
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

        for ( ServedResource resource : registry.findResources( uri ))
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

}