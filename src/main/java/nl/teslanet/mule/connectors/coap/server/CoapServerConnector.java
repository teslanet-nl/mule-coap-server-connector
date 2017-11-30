package nl.teslanet.mule.connectors.coap.server;


import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.resource.spi.work.WorkException;

import org.eclipse.californium.core.CoapServer;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;
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
    private static final String COAP_URI_WILDCARD= "*";

    @Config
    private ServerConfig config;

    private CoapServer server= null;

    private HashMap< String, ServedResource > servedResources;

    @Inject
    private MuleContext context;

    private SourceCallback callback= null;

    @Start
    public void startServer() throws ConnectionException, WorkException
    {
        if ( config.getResources() == null || config.getResources().isEmpty() )
        {
            throw new ConnectionException( ConnectionExceptionCode.UNKNOWN, "coap resources not defined", null );
        }
        if ( servedResources == null )
        {
            servedResources= new HashMap< String, ServedResource >();
        }
        else
        {
            servedResources.clear();
        }

        // binds on UDP port 5683
        server= new CoapServer();

        addResources( server, config.getResources() );

        //System.getSecurityManager().checkAccept( "localhost", 5683 );
        //server.addEndpoint(new CoapEndpoint(new InetSocketAddress("0.0.0.0", 5683)));
        server.start();
    }

    private void addResources( CoapServer server, List< ResourceConfig > resourceConfigs )
    {
        for ( ResourceConfig resourceConfig : resourceConfigs )
        {
            ServedResource toserve= new ServedResource( this, resourceConfig );
            server.add( toserve );
            servedResources.put( toserve.getURI(), toserve );
            addChildren( toserve );
        }
    }

    private void addChildren( ServedResource parent )
    {
        for ( ResourceConfig resourceConfig : parent.getConfiguredResource().getResources() )
        {
            ServedResource toserve= new ServedResource( this, resourceConfig );
            parent.add( toserve );
            servedResources.put( toserve.getURI(), toserve );
            addChildren( toserve );
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
        //TODO add option to listen on specific resource
        if ( uri.equals( COAP_URI_WILDCARD ) )
        {
            //set general callback
            if ( this.callback == null )
            {
                this.callback= callback;
            }
            else
            {
                //TODO improve
                throw new Exception( "CoAP URI wildcard can be listened on only once." );
            }
        }
        else
        {
            // set resource specific callback
            ServedResource toListenOn= servedResources.get( uri );
            if ( toListenOn != null && !toListenOn.hasOwnCallback() )
            {
                toListenOn.setCallback( callback );
            }
            else
            {
                //TODO improve
                throw new Exception( "CoAP URI cannot be listened on." );
            }
        }
    }

    @Processor
    public void resourceChanged( String uri ) throws Exception
    {
        if ( uri == null )
        {
            throw new Exception( "CoAP URI cannot be null." );
        }

        if ( uri.equals( COAP_URI_WILDCARD ) )
        {
            // all resources are to be considered changed
            for ( ServedResource resource : servedResources.values() )
            {
                resource.changed();
            }
        }
        else
        {
            ServedResource resource= servedResources.get( uri );

            if ( resource != null )
            {
                //specified resource has changed
                resource.changed();
            }
            else
            {
                throw new Exception( "CoAP URI is not a resource." );
            }
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

    public SourceCallback getCallback()
    {
        return callback;
    }

    public void setCallback( SourceCallback callback )
    {
        this.callback= callback;
    }

    /**
     * @return the Mule context
     */
    public MuleContext getContext()
    {
        return context;
    }

}