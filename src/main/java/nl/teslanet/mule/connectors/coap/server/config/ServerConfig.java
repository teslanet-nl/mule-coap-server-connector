package nl.teslanet.mule.connectors.coap.server.config;

import org.mule.api.annotations.components.Configuration;

import java.util.List;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;

@Configuration(friendlyName = "CoAP Server Connector configuration")
public class ServerConfig 
{

    private List< ResourceConfig > resourceConfigs;

    public List< ResourceConfig > getResources()
    {
        return resourceConfigs;
    }

    public void setResources( List< ResourceConfig > resourceConfigs )
    {
        this.resourceConfigs = resourceConfigs;
    }

}