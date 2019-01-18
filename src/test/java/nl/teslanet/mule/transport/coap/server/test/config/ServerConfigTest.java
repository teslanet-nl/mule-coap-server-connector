package nl.teslanet.mule.transport.coap.server.test.config;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.eclipse.californium.core.network.config.NetworkConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.server.config.DeduplicatorName;
import nl.teslanet.mule.transport.coap.server.config.ServerConfig;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;
import nl.teslanet.mule.transport.coap.server.test.config.Properties.PropertyName;


public class ServerConfigTest
{

    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {

    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testConstructor() throws ResourceUriException
    {
        ServerConfig config= new ServerConfig();
        assertNotNull( "constructor deliverd null", config );
    }

    @Test
    public void testGettersAndSetters() throws Exception
    {
        ServerConfig config= new ServerConfig();

        for ( ConfigPropertyDesc prop : getConfigProperties() )
        {
            String input;
            String output;

            input= prop.getCustomValue();
            Properties.setValue( prop.getPropertyName(), config, input );
            output= Properties.getValue( prop.getPropertyName(), config );
            assertEquals( "get/set failed on " + prop.getPropertyName(), input, output );
        }
    }

    @Test
    public void testDefaults() throws Exception
    {
        ServerConfig config= new ServerConfig();

        for ( ConfigPropertyDesc prop : getConfigProperties() )
        {
            String defaultValue;
            String output;

            defaultValue= prop.getExpectedDefaultValue();
            output= Properties.getValue( prop.getPropertyName(), config );
            assertEquals( "got wrong default value for " + prop.getPropertyName(), defaultValue, output );
        }
    }

    @Test
    public void testDefaultNetworkValues() throws Exception
    {
        ServerConfig config= new ServerConfig();

        for ( ConfigPropertyDesc prop : getConfigProperties() )
        {
            if ( Properties.isNetworkProperty( prop.getPropertyName() ) )
            {
                String input;
                String output;

                input= prop.getExpectedDefaultNetworkValue();
                output= Properties.getNetworkValue( prop.getPropertyName(), config );
                assertEquals( "got wrong network value for " + prop.getPropertyName(), input, output );
            }
        }
    }

    @Test
    public void testCustomNetworkValues() throws Exception
    {
        ServerConfig config= new ServerConfig();

        for ( ConfigPropertyDesc prop : getConfigProperties() )
        {
            if ( Properties.isNetworkProperty( prop.getPropertyName() ) )
            {
                String input;
                String output;

                input= prop.getCustomValue();
                Properties.setValue( prop.getPropertyName(), config, input );
                String expected= prop.getExpectedCustomNetworkValue();
                output= Properties.getNetworkValue( prop.getPropertyName(), config );
                assertEquals( "got wrong network value for " + prop.getPropertyName(), expected, output );
            }
        }
    }

    private ArrayList< ConfigPropertyDesc > getConfigProperties()
    {
        ArrayList< ConfigPropertyDesc > list= new ArrayList< ConfigPropertyDesc >();

        list.add( new ConfigPropertyDesc( PropertyName.secure, null, "false", null, "true", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.logCoapMessages, null, "false", null, "true", null ) );
        //from EndpointConfig
        list.add( new ConfigPropertyDesc( PropertyName.bindToHost, null, null, "somehost.org", null, null ) );
        list.add( new ConfigPropertyDesc( PropertyName.bindToPort, NetworkConfig.Keys.COAP_PORT, null, "5683", "9983", "9983" ) );
        list.add( new ConfigPropertyDesc( PropertyName.bindToSecurePort, NetworkConfig.Keys.COAP_SECURE_PORT, null, "5684", "9984", "9984" ) );
        list.add( new ConfigPropertyDesc( PropertyName.keyStoreLocation, null, null, null, "/tmp/test1", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.keyStorePassword, null, null, null, "secret1", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.trustStoreLocation, null, null, null, "/tmp/test2", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.trustStorePassword, null, null,null,  "secret1", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.privateKeyAlias, null, null, null, "secretKey", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.trustedRootCertificateAlias, null, null, null, "certificate2", null ) );
        list.add( new ConfigPropertyDesc( PropertyName.ackTimeout, NetworkConfig.Keys.ACK_TIMEOUT, null, "2000", "22000", "22000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.ackRandomFactor, NetworkConfig.Keys.ACK_RANDOM_FACTOR, null, "1.5", "3.56", "3.56" ) );
        list.add( new ConfigPropertyDesc( PropertyName.ackTimeoutScale, NetworkConfig.Keys.ACK_TIMEOUT_SCALE, null, "2.0", "7.364",  "7.364" ) );
        list.add( new ConfigPropertyDesc( PropertyName.maxRetransmit, NetworkConfig.Keys.MAX_RETRANSMIT, null, "4", "44", "44" ) );
        list.add( new ConfigPropertyDesc( PropertyName.exchangeLifetime, NetworkConfig.Keys.EXCHANGE_LIFETIME, null, "247000", "3000000", "3000000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.nonLifetime, NetworkConfig.Keys.NON_LIFETIME, null, "145000", "755000", "755000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.maxTransmitWait, NetworkConfig.Keys.MAX_TRANSMIT_WAIT, null, "93000", "158000", "158000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.nstart, NetworkConfig.Keys.NSTART, null, "1", "145", "145" ) );
        list.add( new ConfigPropertyDesc( PropertyName.leisure,  NetworkConfig.Keys.LEISURE, null, "5000", "9000", "9000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.probingRate, NetworkConfig.Keys.PROBING_RATE, null, "1.0", "3.15", "3.15" ) );
        list.add( new ConfigPropertyDesc( PropertyName.useRandomMidStart, NetworkConfig.Keys.USE_RANDOM_MID_START, null, "true", "false", "false" ) );
        list.add( new ConfigPropertyDesc( PropertyName.tokenSizeLimit, NetworkConfig.Keys.TOKEN_SIZE_LIMIT, null, "8", "15", "15" ) );
        list.add( new ConfigPropertyDesc( PropertyName.preferredBlockSize, NetworkConfig.Keys.PREFERRED_BLOCK_SIZE, null, "512", "1024", "1024" ) );
        list.add( new ConfigPropertyDesc( PropertyName.maxMessageSize, NetworkConfig.Keys.MAX_MESSAGE_SIZE, null, "1024", "4156", "4156" ) );
        list.add( new ConfigPropertyDesc( PropertyName.blockwiseStatusLifetime, NetworkConfig.Keys.BLOCKWISE_STATUS_LIFETIME, null, "300000", "150000", "150000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.notificationCheckIntervalTime, NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME, null, "86400000", "91100001", "91100001" ) );
        list.add( new ConfigPropertyDesc( PropertyName.notificationCheckIntervalCount, NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT, null, "100", "95", "95" ) );
        list.add( new ConfigPropertyDesc( PropertyName.notificationReregistrationBackoff, NetworkConfig.Keys.NOTIFICATION_REREGISTRATION_BACKOFF, null, "2000", "5002", "5002" ) );
        list.add( new ConfigPropertyDesc( PropertyName.useCongestionControl, NetworkConfig.Keys.USE_CONGESTION_CONTROL, null, "false", "true", "true" ) );
        list.add( new ConfigPropertyDesc( PropertyName.congestionControlAlgorithm, NetworkConfig.Keys.CONGESTION_CONTROL_ALGORITHM, null, "Cocoa", "Cocoala2", "Cocoala2" ) );
        list.add( new ConfigPropertyDesc( PropertyName.protocolStageThreadCount, NetworkConfig.Keys.PROTOCOL_STAGE_THREAD_COUNT, null, "4", "12", "12" ) );
        list.add( new ConfigPropertyDesc( PropertyName.networkStageReceiverThreadCount, NetworkConfig.Keys.NETWORK_STAGE_RECEIVER_THREAD_COUNT, null, "1", "12", "12" ) );
        list.add( new ConfigPropertyDesc( PropertyName.networkStageSenderThreadCount, NetworkConfig.Keys.NETWORK_STAGE_SENDER_THREAD_COUNT, null, "1", "18", "18" ) );
        list.add( new ConfigPropertyDesc( PropertyName.udpConnectorDatagramSize, NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE, null, "2048", "4096", "4096" ) );
        list.add( new ConfigPropertyDesc( PropertyName.udpConnectorReceiveBuffer, NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER, null, "0", "1000", "1000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.udpConnectorSendBuffer, NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER, null, "0", "500", "500" ) );
        list.add( new ConfigPropertyDesc( PropertyName.udpConnectorOutCapacity, NetworkConfig.Keys.UDP_CONNECTOR_OUT_CAPACITY, null, "2147483647", "1007483647", "1007483647" ) );
        list.add( new ConfigPropertyDesc( PropertyName.deduplicator, NetworkConfig.Keys.DEDUPLICATOR, null, NetworkConfig.Keys.DEDUPLICATOR_MARK_AND_SWEEP,DeduplicatorName.CROP_ROTATION.name(),NetworkConfig.Keys.DEDUPLICATOR_CROP_ROTATION ) );
        list.add( new ConfigPropertyDesc( PropertyName.markAndSweepInterval, NetworkConfig.Keys.MARK_AND_SWEEP_INTERVAL, null, "10000", "22000", "22000" ) );
        list.add( new ConfigPropertyDesc( PropertyName.cropRotationPeriod, NetworkConfig.Keys.CROP_ROTATION_PERIOD, null, "2000", "7800", "7800" ) );
        list.add( new ConfigPropertyDesc( PropertyName.healthStatusInterval, NetworkConfig.Keys.HEALTH_STATUS_INTERVAL, null, "0", "100", "100" ) );

        return list;
    }
}
