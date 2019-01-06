package nl.teslanet.mule.transport.coap.server.test.modules;


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
            prop.setValue( config, input );
            output= prop.getValue( config );
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

            defaultValue= prop.getDefaultValue();
            output= prop.getValue( config );
            assertEquals( "got wrong default value for " + prop.getPropertyName(), defaultValue, output );
        }
    }

    @Test
    public void testDefaultNetworkValues() throws Exception
    {
        ServerConfig config= new ServerConfig();

        for ( ConfigPropertyDesc prop : getConfigProperties() )
        {
            if ( prop.isNetworkProperty() )
            {
                String input;
                String output;

                input= prop.getDefaultNetworkValue();
                output= prop.getNetworkValue( config );
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
            if ( prop.isNetworkProperty() )
            {
                String input;
                String output;

                input= prop.getCustomValue();
                prop.setValue( config, input );
                output= prop.getNetworkValue( config );
                assertEquals( "got wrong network value for " + prop.getPropertyName(), input, output );
            }
        }
    }

    /**
     * Description of a property to test
     *
     */
    public static class ConfigPropertyDesc
    {
        public enum PropertyName
        {
            secure, logCoapMessages,
            //from EndpointConfig
            bindToHost, bindToPort, bindToSecurePort, ackTimeout, ackRandomFactor, ackTimeoutScale, maxRetransmit, exchangeLifetime, nonLifetime, maxTransmitWait, nstart, leisure, probingRate, keyStoreLocation, keyStorePassword, privateKeyAlias, trustStoreLocation, trustStorePassword, trustedRootCertificateAlias, useRandomMidStart, tokenSizeLimit, preferredBlockSize, maxMessageSize, blockwiseStatusLifetime, notificationCheckIntervalTime, notificationCheckIntervalCount, notificationReregistrationBackoff, useCongestionControl, congestionControlAlgorithm, protocolStageThreadCount, networkStageReceiverThreadCount, networkStageSenderThreadCount, udpConnectorDatagramSize, udpConnectorReceiveBuffer, udpConnectorSendBuffer, udpConnectorOutCapacity,
            //
            deduplicator,
            //deduplicatorMarkAndSweep, 
            markAndSweepInterval,
            //deduplicatorCropRotation, 
            cropRotationPeriod,
            //noDeduplicator,
            //httpPort,
            // httpServerSocketTimeout,
            // httpServerSocketBufferSize,
            // httpCacheResponseMaxAge,
            // httpCacheSize,
            healthStatusInterval
        }

        private PropertyName propertyName;

        private String defaultValue;

        private String defaultNetworkValue;

        private String customValue;

        /**
         * Construct the c
         * @param propertyName
         * @param defaultValue
         * @param customValue
         */
        public ConfigPropertyDesc( PropertyName propertyName, String defaultValue, String defaultNetworkValue, String customValue )
        {
            this.propertyName= propertyName;
            this.defaultValue= defaultValue;
            this.defaultNetworkValue= defaultNetworkValue;
            this.customValue= customValue;
        }

        /**
         * @return the propertyName
         */
        public PropertyName getPropertyName()
        {
            return propertyName;
        }

        /**
         * @return the defaultValue
         */
        public String getDefaultValue()
        {
            return defaultValue;
        }
        
        /**
         * @return the defaultNetworkValue
         */
        public String getDefaultNetworkValue()
        {
            return defaultNetworkValue;
        }

        /**
         * @return the customValue
         */
        public String getCustomValue()
        {
            return customValue;
        }


        /**
         * Get configured value for the property
         * @param config
         * @return
         * @throws Exception
         */
        public String getValue( ServerConfig config ) throws Exception
        {
            String result= null;

            switch ( propertyName )
            {
                case secure:
                    result= Boolean.toString( config.isSecure() );
                    break;
                case logCoapMessages:
                    result= Boolean.toString( config.isLogCoapMessages() );
                    break;
                case bindToHost:
                    result= config.getBindToHost();
                    break;
                case bindToPort:
                    result= config.getBindToPort();
                    break;
                case bindToSecurePort:
                    result= config.getBindToSecurePort();
                    break;
                case keyStoreLocation:
                    result= config.getKeyStoreLocation();
                    break;
                case keyStorePassword:
                    result= config.getKeyStorePassword();
                    break;
                case trustStoreLocation:
                    result= config.getTrustStoreLocation();
                    break;
                case trustStorePassword:
                    result= config.getTrustStorePassword();
                    break;
                case privateKeyAlias:
                    result= config.getPrivateKeyAlias();
                    break;
                case trustedRootCertificateAlias:
                    result= config.getTrustedRootCertificateAlias();
                    break;
                case ackTimeout:
                    result= config.getAckTimeout();
                    break;
                case ackRandomFactor:
                    result= config.getAckRandomFactor();
                    break;
                case ackTimeoutScale:
                    result= config.getAckTimeoutScale();
                    break;
                case maxRetransmit:
                    result= config.getMaxRetransmit();
                    break;
                case exchangeLifetime:
                    result= config.getExchangeLifetime();
                    break;
                case nonLifetime:
                    result= config.getNonLifetime();
                    break;
                case maxTransmitWait:
                    result= config.getMaxTransmitWait();
                    break;
                case nstart:
                    result= config.getNstart();
                    break;
                case leisure:
                    result= config.getLeisure();
                    break;
                case probingRate:
                    result= config.getProbingRate();
                    break;
                case useRandomMidStart:
                    result= config.getUseRandomMidStart();
                    break;
                case tokenSizeLimit:
                    result= config.getTokenSizeLimit();
                    break;
                case preferredBlockSize:
                    result= config.getPreferredBlockSize();
                    break;
                case maxMessageSize:
                    result= config.getMaxMessageSize();
                    break;
                case blockwiseStatusLifetime:
                    result= config.getBlockwiseStatusLifetime();
                    break;
                case notificationCheckIntervalTime:
                    result= config.getNotificationCheckIntervalTime();
                    break;
                case notificationCheckIntervalCount:
                    result= config.getNotificationCheckIntervalCount();
                    break;
                case notificationReregistrationBackoff:
                    result= config.getNotificationReregistrationBackoff();
                    break;
                case useCongestionControl:
                    result= config.getUseCongestionControl();
                    break;
                case congestionControlAlgorithm:
                    result= config.getCongestionControlAlgorithm();
                    break;
                case protocolStageThreadCount:
                    result= config.getProtocolStageThreadCount();
                    break;
                case networkStageReceiverThreadCount:
                    result= config.getNetworkStageReceiverThreadCount();
                    break;
                case networkStageSenderThreadCount:
                    result= config.getNetworkStageSenderThreadCount();
                    break;
                case udpConnectorDatagramSize:
                    result= config.getUdpConnectorDatagramSize();
                    break;
                case udpConnectorReceiveBuffer:
                    result= config.getUdpConnectorReceiveBuffer();
                    break;
                case udpConnectorSendBuffer:
                    result= config.getUdpConnectorSendBuffer();
                    break;
                case udpConnectorOutCapacity:
                    result= config.getUdpConnectorOutCapacity();
                    break;
                case deduplicator:
                    result= ( config.getDeduplicator() != null ? config.getDeduplicator().name() : null );
                    break;
                // used by californium as property value
                //                case deduplicatorMarkAndSweep:
                //                    result= config.getDeduplicatorMarkAndSweep();
                //                    break;
                case markAndSweepInterval:
                    result= config.getMarkAndSweepInterval();
                    break;
                // used by californium as property value
                //                case deduplicatorCropRotation:
                //                    result= config.getDeduplicatorCropRotation();
                //                    break;
                case cropRotationPeriod:
                    result= config.getCropRotationPeriod();
                    break;
                // used by californium as property value
                //                case noDeduplicator:
                //                    result= config.getNoDeduplicator();
                //                    break;
                case healthStatusInterval:
                    result= config.getHealthStatusInterval();
                    break;
                default:
                    throw new Exception( "propertyname unknown" );
            }
            return result;
        }


        /**
         * Set configured value for the property
         * @param config configuration to set the property on
         * @param value to set
         * @throws Exception when property is invalid
         */
        public void setValue( ServerConfig config, String value ) throws Exception
        {
            switch ( propertyName )
            {
                case secure:
                    config.setSecure( Boolean.parseBoolean( value ) );
                    break;
                case logCoapMessages:
                    config.setLogCoapMessages( Boolean.parseBoolean( value ) );
                    break;
                case bindToHost:
                    config.setBindToHost( value );
                    break;
                case bindToPort:
                    config.setBindToPort( value );
                    break;
                case bindToSecurePort:
                    config.setBindToSecurePort( value );
                    break;
                case keyStoreLocation:
                    config.setKeyStoreLocation( value );
                    break;
                case keyStorePassword:
                    config.setKeyStorePassword( value );
                    break;
                case trustStoreLocation:
                    config.setTrustStoreLocation( value );
                    break;
                case trustStorePassword:
                    config.setTrustStorePassword( value );
                    break;
                case privateKeyAlias:
                    config.setPrivateKeyAlias( value );
                    break;
                case trustedRootCertificateAlias:
                    config.setTrustedRootCertificateAlias( value );
                    break;
                case ackTimeout:
                    config.setAckTimeout( value );
                    break;
                case ackRandomFactor:
                    config.setAckRandomFactor( value );
                    break;
                case ackTimeoutScale:
                    config.setAckTimeoutScale( value );
                    break;
                case maxRetransmit:
                    config.setMaxRetransmit( value );
                    break;
                case exchangeLifetime:
                    config.setExchangeLifetime( value );
                    break;
                case nonLifetime:
                    config.setNonLifetime( value );
                    break;
                case maxTransmitWait:
                    config.setMaxTransmitWait( value );
                    break;
                case nstart:
                    config.setNstart( value );
                    break;
                case leisure:
                    config.setLeisure( value );
                    break;
                case probingRate:
                    config.setProbingRate( value );
                    break;
                case useRandomMidStart:
                    config.setUseRandomMidStart( value );
                    break;
                case tokenSizeLimit:
                    config.setTokenSizeLimit( value );
                    break;
                case preferredBlockSize:
                    config.setPreferredBlockSize( value );
                    break;
                case maxMessageSize:
                    config.setMaxMessageSize( value );
                    break;
                case blockwiseStatusLifetime:
                    config.setBlockwiseStatusLifetime( value );
                    break;
                case notificationCheckIntervalTime:
                    config.setNotificationCheckIntervalTime( value );
                    break;
                case notificationCheckIntervalCount:
                    config.setNotificationCheckIntervalCount( value );
                    break;
                case notificationReregistrationBackoff:
                    config.setNotificationReregistrationBackoff( value );
                    break;
                case useCongestionControl:
                    config.setUseCongestionControl( value );
                    break;
                case congestionControlAlgorithm:
                    config.setCongestionControlAlgorithm( value );
                    break;
                case protocolStageThreadCount:
                    config.setProtocolStageThreadCount( value );
                    break;
                case networkStageReceiverThreadCount:
                    config.setNetworkStageReceiverThreadCount( value );
                    break;
                case networkStageSenderThreadCount:
                    config.setNetworkStageSenderThreadCount( value );
                    break;
                case udpConnectorDatagramSize:
                    config.setUdpConnectorDatagramSize( value );
                    break;
                case udpConnectorReceiveBuffer:
                    config.setUdpConnectorReceiveBuffer( value );
                    break;
                case udpConnectorSendBuffer:
                    config.setUdpConnectorSendBuffer( value );
                    break;
                case udpConnectorOutCapacity:
                    config.setUdpConnectorOutCapacity( value );
                    break;
                case deduplicator:
                    config.setDeduplicator( DeduplicatorName.valueOf( value ) );
                    break;
                // used by californium as property value
                //                case deduplicatorMarkAndSweep:
                //                    config.setDeduplicatorMarkAndSweep( value );
                //                    break;
                case markAndSweepInterval:
                    config.setMarkAndSweepInterval( value );
                    break;
                // used by californium as property value
                //                case deduplicatorCropRotation:
                //                    config.setDeduplicatorCropRotation( value );
                //                    break;
                case cropRotationPeriod:
                    config.setCropRotationPeriod( value );
                    break;
                // used by californium as property value
                //                case noDeduplicator:
                //                    config.setNoDeduplicator( value );
                //                    break;
                case healthStatusInterval:
                    config.setHealthStatusInterval( value );
                    break;
                default:
                    throw new Exception( "propertyname unknown" );
            }
        }

        /**
         * @return the key when property is a networkproperty, otherwise null is returned
         * @throws Exception when property is invalid
         */
        public String getKey() throws Exception
        {
            String result= null;

            switch ( propertyName )
            {
                case secure:
                    break;
                case logCoapMessages:
                    break;
                case bindToHost:
                    break;
                case bindToPort:
                    result= NetworkConfig.Keys.COAP_PORT;
                    break;
                case bindToSecurePort:
                    result= NetworkConfig.Keys.COAP_SECURE_PORT;
                    break;
                case ackTimeout:
                    result= NetworkConfig.Keys.ACK_TIMEOUT;
                    break;
                case ackRandomFactor:
                    result= NetworkConfig.Keys.ACK_RANDOM_FACTOR;
                    break;
                case ackTimeoutScale:
                    result= NetworkConfig.Keys.ACK_TIMEOUT_SCALE;
                    break;
                case maxRetransmit:
                    result= NetworkConfig.Keys.MAX_RETRANSMIT;
                    break;
                case exchangeLifetime:
                    result= NetworkConfig.Keys.EXCHANGE_LIFETIME;
                    break;
                case nonLifetime:
                    result= NetworkConfig.Keys.NON_LIFETIME;
                    break;
                case maxTransmitWait:
                    result= NetworkConfig.Keys.MAX_TRANSMIT_WAIT;
                    break;
                case nstart:
                    result= NetworkConfig.Keys.NSTART;
                    break;
                case leisure:
                    result= NetworkConfig.Keys.LEISURE;
                    break;
                case probingRate:
                    result= NetworkConfig.Keys.PROBING_RATE;
                    break;
                case keyStoreLocation:
                    break;
                case keyStorePassword:
                    break;
                case privateKeyAlias:
                    break;
                case trustStoreLocation:
                    break;
                case trustStorePassword:
                    break;
                case trustedRootCertificateAlias:
                    break;
                case useRandomMidStart:
                    result= NetworkConfig.Keys.USE_RANDOM_MID_START;
                    break;
                case tokenSizeLimit:
                    result= NetworkConfig.Keys.TOKEN_SIZE_LIMIT;
                    break;
                case preferredBlockSize:
                    result= NetworkConfig.Keys.PREFERRED_BLOCK_SIZE;
                    break;
                case maxMessageSize:
                    result= NetworkConfig.Keys.MAX_MESSAGE_SIZE;
                    break;
                case blockwiseStatusLifetime:
                    result= NetworkConfig.Keys.BLOCKWISE_STATUS_LIFETIME;
                    break;
                case notificationCheckIntervalTime:
                    result= NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME;
                    break;
                case notificationCheckIntervalCount:
                    result= NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT;
                    break;
                case notificationReregistrationBackoff:
                    result= NetworkConfig.Keys.NOTIFICATION_REREGISTRATION_BACKOFF;
                    break;
                case useCongestionControl:
                    result= NetworkConfig.Keys.USE_CONGESTION_CONTROL;
                    break;
                case congestionControlAlgorithm:
                    result= NetworkConfig.Keys.CONGESTION_CONTROL_ALGORITHM;
                    break;
                case protocolStageThreadCount:
                    result= NetworkConfig.Keys.PROTOCOL_STAGE_THREAD_COUNT;
                    break;
                case networkStageReceiverThreadCount:
                    result= NetworkConfig.Keys.NETWORK_STAGE_RECEIVER_THREAD_COUNT;
                    break;
                case networkStageSenderThreadCount:
                    result= NetworkConfig.Keys.NETWORK_STAGE_SENDER_THREAD_COUNT;
                    break;
                case udpConnectorDatagramSize:
                    result= NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE;
                    break;
                case udpConnectorReceiveBuffer:
                    result= NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER;
                    break;
                case udpConnectorSendBuffer:
                    result= NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER;
                    break;
                case udpConnectorOutCapacity:
                    result= NetworkConfig.Keys.UDP_CONNECTOR_OUT_CAPACITY;
                    break;
                case deduplicator:
                    result= NetworkConfig.Keys.DEDUPLICATOR;
                    break;
                // used by californium as property value
                //                case deduplicatorMarkAndSweep:
                //                    result= NetworkConfig.Keys.DEDUPLICATOR_MARK_AND_SWEEP;
                //                    break;
                case markAndSweepInterval:
                    result= NetworkConfig.Keys.MARK_AND_SWEEP_INTERVAL;
                    break;
                // used by californium as property value
                //                case deduplicatorCropRotation:
                //                    result= NetworkConfig.Keys.DEDUPLICATOR_CROP_ROTATION;
                //                    break;
                case cropRotationPeriod:
                    result= NetworkConfig.Keys.CROP_ROTATION_PERIOD;
                    break;
                // used by californium as property value
                //                case noDeduplicator:
                //                    result= NetworkConfig.Keys.NO_DEDUPLICATOR;
                //                    break;
                //case httpPort: result= NetworkConfig.Keys.httpPort ; break;
                // case httpServerSocketTimeout: result= NetworkConfig.Keys.httpServerSocketTimeout ; break;
                // case httpServerSocketBufferSize: result= NetworkConfig.Keys.httpServerSocketBufferSize ; break;
                // case httpCacheResponseMaxAge: result= NetworkConfig.Keys.httpCacheResponseMaxAge ; break;
                // case httpCacheSize: result= NetworkConfig.Keys.httpCacheSize ; break;
                case healthStatusInterval:
                    result= NetworkConfig.Keys.HEALTH_STATUS_INTERVAL;
                    break;

                default:
                    throw new Exception( "propertyname unknown" );
            }
            return result;
        }

        /**
         * @param networkPropertyKey the networkPropertyKey to set
         * @throws Exception when invalid property
         */
        boolean isNetworkProperty() throws Exception
        {
            return( getKey() != null );
        }

        /**
         * @return the value
         * @throws Exception 
         */
        public String getNetworkValue( ServerConfig config ) throws Exception
        {
            String key= null;
            String result= null;

            switch ( propertyName )
            {
                case secure:
                    break;
                case logCoapMessages:
                    break;
                case bindToHost:
                    break;
                case bindToPort:
                    key= NetworkConfig.Keys.COAP_PORT;
                    break;
                case bindToSecurePort:
                    key= NetworkConfig.Keys.COAP_SECURE_PORT;
                    break;
                case keyStoreLocation:
                    break;
                case keyStorePassword:
                    break;
                case trustStoreLocation:
                    break;
                case trustStorePassword:
                    break;
                case privateKeyAlias:
                    break;
                case trustedRootCertificateAlias:
                    break;
                case ackTimeout:
                    key= NetworkConfig.Keys.ACK_TIMEOUT;
                    break;
                case ackRandomFactor:
                    key= NetworkConfig.Keys.ACK_RANDOM_FACTOR;
                    break;
                case ackTimeoutScale:
                    key= NetworkConfig.Keys.ACK_TIMEOUT_SCALE;
                    break;
                case maxRetransmit:
                    key= NetworkConfig.Keys.MAX_RETRANSMIT;
                    break;
                case exchangeLifetime:
                    key= NetworkConfig.Keys.EXCHANGE_LIFETIME;
                    break;
                case nonLifetime:
                    key= NetworkConfig.Keys.NON_LIFETIME;
                    break;
                case maxTransmitWait:
                    key= NetworkConfig.Keys.MAX_TRANSMIT_WAIT;
                    break;
                case nstart:
                    key= NetworkConfig.Keys.NSTART;
                    break;
                case leisure:
                    key= NetworkConfig.Keys.LEISURE;
                    break;
                case probingRate:
                    key= NetworkConfig.Keys.PROBING_RATE;
                    break;
                case useRandomMidStart:
                    key= NetworkConfig.Keys.USE_RANDOM_MID_START;
                    break;
                case tokenSizeLimit:
                    key= NetworkConfig.Keys.TOKEN_SIZE_LIMIT;
                    break;
                case preferredBlockSize:
                    key= NetworkConfig.Keys.PREFERRED_BLOCK_SIZE;
                    break;
                case maxMessageSize:
                    key= NetworkConfig.Keys.MAX_MESSAGE_SIZE;
                    break;
                case blockwiseStatusLifetime:
                    key= NetworkConfig.Keys.BLOCKWISE_STATUS_LIFETIME;
                    break;
                case notificationCheckIntervalTime:
                    key= NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME;
                    break;
                case notificationCheckIntervalCount:
                    key= NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT;
                    break;
                case notificationReregistrationBackoff:
                    key= NetworkConfig.Keys.NOTIFICATION_REREGISTRATION_BACKOFF;
                    break;
                case useCongestionControl:
                    key= NetworkConfig.Keys.USE_CONGESTION_CONTROL;
                    break;
                case congestionControlAlgorithm:
                    key= NetworkConfig.Keys.CONGESTION_CONTROL_ALGORITHM;
                    break;
                case protocolStageThreadCount:
                    key= NetworkConfig.Keys.PROTOCOL_STAGE_THREAD_COUNT;
                    break;
                case networkStageReceiverThreadCount:
                    key= NetworkConfig.Keys.NETWORK_STAGE_RECEIVER_THREAD_COUNT;
                    break;
                case networkStageSenderThreadCount:
                    key= NetworkConfig.Keys.NETWORK_STAGE_SENDER_THREAD_COUNT;
                    break;
                case udpConnectorDatagramSize:
                    key= NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE;
                    break;
                case udpConnectorReceiveBuffer:
                    key= NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER;
                    break;
                case udpConnectorSendBuffer:
                    key= NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER;
                    break;
                case udpConnectorOutCapacity:
                    key= NetworkConfig.Keys.UDP_CONNECTOR_OUT_CAPACITY;
                    break;
                case deduplicator:
                    key= NetworkConfig.Keys.DEDUPLICATOR;
                    break;
                // used by californium as property value
                //                case deduplicatorMarkAndSweep:
                //                    key= NetworkConfig.Keys.DEDUPLICATOR_MARK_AND_SWEEP;
                //                    break;
                case markAndSweepInterval:
                    key= NetworkConfig.Keys.MARK_AND_SWEEP_INTERVAL;
                    break;
                // used by californium as property value
                //                case deduplicatorCropRotation:
                //                    key= NetworkConfig.Keys.DEDUPLICATOR_CROP_ROTATION;
                //                    break;
                case cropRotationPeriod:
                    key= NetworkConfig.Keys.CROP_ROTATION_PERIOD;
                    break;
                // used by californium as property value
                //                case noDeduplicator:
                //                    key= NetworkConfig.Keys.NO_DEDUPLICATOR;
                //                    break;
                case healthStatusInterval:
                    key= NetworkConfig.Keys.HEALTH_STATUS_INTERVAL;
                    break;
                default:
                    throw new Exception( "propertyname unknown" );
            }
            if ( key != null )
            {
                result= config.getNetworkConfig().getString( key );
            }
            return result;
        }
    }

    private ArrayList< ConfigPropertyDesc > getConfigProperties()
    {
        ArrayList< ConfigPropertyDesc > list= new ArrayList< ConfigPropertyDesc >();

        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.secure, "false",  "false", "true" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.logCoapMessages, "false", "false", "true" ) );
        //from EndpointConfig
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.bindToHost, null,  null, "somehost.org" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.bindToPort, null,  "5683", "9983" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.bindToSecurePort, null,  "5684", "9984" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.keyStoreLocation, null,  null, "/tmp/test1" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.keyStorePassword, null,  null, "secret1" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.trustStoreLocation, null,  null, "/tmp/test2" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.trustStorePassword, null,  null, "secret1" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.privateKeyAlias, null,  null, "secretKey" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.trustedRootCertificateAlias, null,  null, "certificate2" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.ackTimeout, null,  "2000", "22000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.ackRandomFactor, null,  "1.5", "3.56" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.ackTimeoutScale, null,  "2.0", "7.364" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.maxRetransmit, null,  "4", "44" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.exchangeLifetime, null,  "247000", "3000000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.nonLifetime, null,  "145000", "755000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.maxTransmitWait, null,  "93000", "158000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.nstart, null,  "1", "145" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.leisure, null,  "5000", "9000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.probingRate, null,  "1.0", "3.15" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.useRandomMidStart, null,  "true", "false" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.tokenSizeLimit, null,  "8", "15" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.preferredBlockSize, null,  "512", "1024" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.maxMessageSize, null,  "1024", "4156" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.blockwiseStatusLifetime, null,  "300000", "150000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.notificationCheckIntervalTime, null,  "86400000", "91100001" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.notificationCheckIntervalCount, null,  "100", "95" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.notificationReregistrationBackoff, null,  "2000", "5002" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.useCongestionControl, null,  "false", "true" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.congestionControlAlgorithm, null,  "Cocoa", "Cocoala2" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.protocolStageThreadCount, null,  "4", "12" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.networkStageReceiverThreadCount, null,  "1", "12" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.networkStageSenderThreadCount, null,  "1", "18" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.udpConnectorDatagramSize, null,  "2048", "4096" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.udpConnectorReceiveBuffer, null,  "0", "1000" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.udpConnectorSendBuffer, null,  "0", "500" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.udpConnectorOutCapacity, null,  "2147483647", "1007483647" ) );
        list.add(
            new ConfigPropertyDesc(
                ConfigPropertyDesc.PropertyName.deduplicator, null, 
                DeduplicatorName.MARK_AND_SWEEP.name(),
                DeduplicatorName.CROP_ROTATION.name() ) );
        //        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.deduplicatorMarkAndSweep, null,  null, "deduplicatorMarkAndSweep2" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.markAndSweepInterval, null,  "10000", "22000" ) );
        //        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.deduplicatorCropRotation, null,  null, "deduplicatorCropRotation2" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.cropRotationPeriod, null,  "2000", "7800" ) );
        //        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.noDeduplicator, null,  null, "true" ) );
        list.add( new ConfigPropertyDesc( ConfigPropertyDesc.PropertyName.healthStatusInterval, null,  "0", "100" ) );

        return list;
    }
}
