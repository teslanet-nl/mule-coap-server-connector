package nl.teslanet.mule.connectors.coap.server.error;


import java.util.logging.Level;
import java.util.logging.Logger;


import org.eclipse.californium.core.network.config.NetworkConfig;
import org.mule.api.annotations.Handle;
import org.mule.api.annotations.components.Handler;

@Handler
public class ErrorHandler {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getCanonicalName());

    @Handle
    public void handle(Exception ex) throws Exception {
    	LOGGER.log( Level.SEVERE, ex.getMessage(), ex );
    	
    }
}