package nl.teslanet.mule.connectors.coap.server.config;

import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;

import java.util.List;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;

@Configuration(friendlyName = "CoAP Server Configuration")
public class ServerConfig 
{
    @Configurable
    @Default(value= "60000")
    int notificationConfirmPeriod;
    


    /**
     * @return the notificationConfirmPeriod
     */
    public int getNotificationConfirmPeriod()
    {
        return notificationConfirmPeriod;
    }

    /**
     * @param notificationConfirmPeriod the notificationConfirmPeriod to set
     */
    public void setNotificationConfirmPeriod( int notificationConfirmPeriod )
    {
        this.notificationConfirmPeriod= notificationConfirmPeriod;
    }

}