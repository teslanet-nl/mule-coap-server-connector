package nl.teslanet.mule.connectors.coap.server;


import java.util.List;

import javax.inject.Inject;
import javax.resource.spi.work.WorkException;

import org.eclipse.californium.core.CoapServer;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.MuleContext;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceThreadingModel;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.lifecycle.Stop;
import org.mule.api.callback.SourceCallback;

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
    private ServerConfig config;

    @Configurable
    private List< Resource > resources;

    private CoapServer server= null;

    @Inject
    private MuleContext context;

    private SourceCallback callback;

    @Start
    public void startServer() throws ConnectionException, WorkException
    {
        if ( resources == null || resources.isEmpty() )
        {
            throw new ConnectionException( ConnectionExceptionCode.UNKNOWN, "coap resources not defined", null );
        }
        // binds on UDP port 5683
        server= new CoapServer();
        for ( Resource resource : resources )
        {
            server.add( new ServedResource( this, resource ));
        }
        //System.getSecurityManager().checkAccept( "localhost", 5683 );
        //server.addEndpoint(new CoapEndpoint(new InetSocketAddress("0.0.0.0", 5683)));
        server.start();
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
    public void listen( SourceCallback callback )
    {
        setCallback( callback );
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

    public List< Resource > getResources()
    {
        return resources;
    }

    public void setResources( List< Resource > resourcedefs )
    {
        this.resources= resourcedefs;
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
     * @return the context
     */
    public MuleContext getContext()
    {
        return context;
    }

}