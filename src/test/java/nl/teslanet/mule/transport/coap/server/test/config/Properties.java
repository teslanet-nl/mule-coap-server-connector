package nl.teslanet.mule.transport.coap.server.test.config;


import org.eclipse.californium.core.network.config.NetworkConfig;

import nl.teslanet.mule.transport.coap.server.config.DeduplicatorName;
import nl.teslanet.mule.transport.coap.server.config.ServerConfig;


/**
 * The properties to test
 *
 */
public class Properties
{
    /**
     * Names of the properties 
     *
     */
    public enum PropertyName
    {
        //from ServerConfig
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

    /**
     * Get configured value for the property
     * @param config
     * @return
     * @throws Exception
     */
    static public String getValue( PropertyName propertyName, ServerConfig config ) throws Exception
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
            case markAndSweepInterval:
                result= config.getMarkAndSweepInterval();
                break;
            case cropRotationPeriod:
                result= config.getCropRotationPeriod();
                break;
            case healthStatusInterval:
                result= config.getHealthStatusInterval();
                break;
            default:
                throw new Exception( "propertyname unknown" );
        }
        return result;
    }

    /**
     * @return the NetworkConfig key when property is a networkproperty, otherwise null is returned
     * @throws Exception when property is invalid
     */
    static public String getKey( PropertyName propertyName ) throws Exception
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
     * @return the property name corresponding to NetworkConfig key
     * @throws Exception when key is not known
     */
    static public PropertyName getName( String key ) throws Exception
    {
        PropertyName result= null;

        switch ( key )
        {
            case NetworkConfig.Keys.COAP_PORT:
                result= PropertyName.bindToPort;
                break;
            case NetworkConfig.Keys.COAP_SECURE_PORT:
                result= PropertyName.bindToSecurePort;
                break;
            case NetworkConfig.Keys.ACK_TIMEOUT:
                result= PropertyName.ackTimeout;
                break;
            case NetworkConfig.Keys.ACK_RANDOM_FACTOR:
                result= PropertyName.ackRandomFactor;
                break;
            case NetworkConfig.Keys.ACK_TIMEOUT_SCALE:
                result= PropertyName.ackTimeoutScale;
                break;
            case NetworkConfig.Keys.MAX_RETRANSMIT:
                result= PropertyName.maxRetransmit;
                break;
            case NetworkConfig.Keys.EXCHANGE_LIFETIME:
                result= PropertyName.exchangeLifetime;
                break;
            case NetworkConfig.Keys.NON_LIFETIME:
                result= PropertyName.nonLifetime;
                break;
            case NetworkConfig.Keys.MAX_TRANSMIT_WAIT:
                result= PropertyName.maxTransmitWait;
                break;
            case NetworkConfig.Keys.NSTART:
                result= PropertyName.nstart;
                break;
            case NetworkConfig.Keys.LEISURE:
                result= PropertyName.leisure;
                break;
            case NetworkConfig.Keys.PROBING_RATE:
                result= PropertyName.probingRate;
                break;
            case NetworkConfig.Keys.USE_RANDOM_MID_START:
                result= PropertyName.useRandomMidStart;
                break;
            case NetworkConfig.Keys.TOKEN_SIZE_LIMIT:
                result= PropertyName.tokenSizeLimit;
                break;
            case NetworkConfig.Keys.PREFERRED_BLOCK_SIZE:
                result= PropertyName.preferredBlockSize;
                break;
            case NetworkConfig.Keys.MAX_MESSAGE_SIZE:
                result= PropertyName.maxMessageSize;
                break;
            case NetworkConfig.Keys.BLOCKWISE_STATUS_LIFETIME:
                result= PropertyName.blockwiseStatusLifetime;
                break;
            case NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME:
                result= PropertyName.notificationCheckIntervalTime;
                break;
            case NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT:
                result= PropertyName.notificationCheckIntervalCount;
                break;
            case NetworkConfig.Keys.NOTIFICATION_REREGISTRATION_BACKOFF:
                result= PropertyName.notificationReregistrationBackoff;
                break;
            case NetworkConfig.Keys.USE_CONGESTION_CONTROL:
                result= PropertyName.useCongestionControl;
                break;
            case NetworkConfig.Keys.CONGESTION_CONTROL_ALGORITHM:
                result= PropertyName.congestionControlAlgorithm;
                break;
            case NetworkConfig.Keys.PROTOCOL_STAGE_THREAD_COUNT:
                result= PropertyName.protocolStageThreadCount;
                break;
            case NetworkConfig.Keys.NETWORK_STAGE_RECEIVER_THREAD_COUNT:
                result= PropertyName.networkStageReceiverThreadCount;
                break;
            case NetworkConfig.Keys.NETWORK_STAGE_SENDER_THREAD_COUNT:
                result= PropertyName.networkStageSenderThreadCount;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE:
                result= PropertyName.udpConnectorDatagramSize;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER:
                result= PropertyName.udpConnectorReceiveBuffer;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER:
                result= PropertyName.udpConnectorSendBuffer;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_OUT_CAPACITY:
                result= PropertyName.udpConnectorOutCapacity;
                break;
            case NetworkConfig.Keys.DEDUPLICATOR:
                result= PropertyName.deduplicator;
                break;
            // used by californium as property value
            //                case : result= deduplicatorMarkAndSweep:
            //                     NetworkConfig.Keys.DEDUPLICATOR_MARK_AND_SWEEP;
            //                    break;
            case NetworkConfig.Keys.MARK_AND_SWEEP_INTERVAL:
                result= PropertyName.markAndSweepInterval;
                break;
            // used by californium as property value
            //                case : result= deduplicatorCropRotation:
            //                     NetworkConfig.Keys.DEDUPLICATOR_CROP_ROTATION;
            //                    break;
            case NetworkConfig.Keys.CROP_ROTATION_PERIOD:
                result= PropertyName.cropRotationPeriod;
                break;
            // used by californium as property value
            //                case : result= noDeduplicator:
            //                     NetworkConfig.Keys.NO_DEDUPLICATOR;
            //                    break;
            //case : result= httpPort:  NetworkConfig.Keys.httpPort ; break;
            // case : result= httpServerSocketTimeout:  NetworkConfig.Keys.httpServerSocketTimeout ; break;
            // case : result= httpServerSocketBufferSize:  NetworkConfig.Keys.httpServerSocketBufferSize ; break;
            // case : result= httpCacheResponseMaxAge:  NetworkConfig.Keys.httpCacheResponseMaxAge ; break;
            // case : result= httpCacheSize:  NetworkConfig.Keys.httpCacheSize ; break;
            case NetworkConfig.Keys.HEALTH_STATUS_INTERVAL:
                result= PropertyName.healthStatusInterval;
                break;

            default:
                throw new Exception( "key unknown" );
        }
        return result;
    }

    /**
     * @param networkPropertyKey the networkPropertyKey to set
     * @throws Exception when invalid property
     */
    static public boolean isNetworkProperty( PropertyName propertyName ) throws Exception
    {
        return( getKey( propertyName ) != null );
    }

    /**
     * @return the value
     * @throws Exception 
     */
    static public String getNetworkValue( PropertyName propertyName, ServerConfig config ) throws Exception
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

    /**
     * Set configured value for the property
     * @param config configuration to set the property on
     * @param value to set
     * @throws Exception when property is invalid
     */
    static public void setValue( PropertyName propertyName, ServerConfig config, String value ) throws Exception
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
}