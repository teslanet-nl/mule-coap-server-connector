package nl.teslanet.mule.transport.coap.server.test.config;


import org.eclipse.californium.core.network.config.NetworkConfig;

import nl.teslanet.mule.transport.coap.server.config.DeduplicatorName;
import nl.teslanet.mule.transport.coap.server.config.DtlsResponseMatchingName;
import nl.teslanet.mule.transport.coap.server.config.MidTrackerName;
import nl.teslanet.mule.transport.coap.server.config.ServerConfig;


/**
 * The Configuration attributes to test
 *
 */
public class ConfigAttributes
{
    /**
     * Names of the configuration attributes 
     *
     */
    public enum AttributeName
    {
        //from ServerConfig
        secure,
        //
        logCoapMessages,
        //from EndpointConfig
        bindToHost,
        bindToPort,
        bindToSecurePort,
        maxActivePeers,
        maxPeerInactivityPeriod,
        ackTimeout,
        ackRandomFactor,
        ackTimeoutScale,
        maxRetransmit,
        exchangeLifetime,
        nonLifetime,
        maxTransmitWait,
        nstart,
        leisure,
        probingRate,
        keyStoreLocation,
        keyStorePassword,
        privateKeyAlias,
        trustStoreLocation,
        trustStorePassword,
        trustedRootCertificateAlias,
        secureSessionTimeout,
        dtlsAutoResumeTimeout,
        responseMatching,
        useRandomMidStart,
        midTracker,
        midTrackerGroups,
        tokenSizeLimit,
        preferredBlockSize,
        maxMessageSize,
        maxResourceBodySize,
        blockwiseStatusLifetime,
        notificationCheckIntervalTime,
        notificationCheckIntervalCount,
        notificationReregistrationBackoff,
        useCongestionControl,
        congestionControlAlgorithm,
        protocolStageThreadCount,
        networkStageReceiverThreadCount,
        networkStageSenderThreadCount,
        udpConnectorDatagramSize,
        udpConnectorReceiveBuffer,
        udpConnectorSendBuffer,
        udpConnectorOutCapacity,
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
        logHealthStatus,
        healthStatusInterval
    }

    /**
     * Get actual value of the attribute
     * @param config
     * @return
     * @throws Exception
     */
    static public String getValue( AttributeName attributeName, ServerConfig config ) throws Exception
    {
        String result= null;

        switch ( attributeName )
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
            case maxActivePeers:
                result= config.getMaxActivePeers();
                break;
            case maxPeerInactivityPeriod:
                result= config.getMaxPeerInactivityPeriod();
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
            case secureSessionTimeout:
                result= config.getSecureSessionTimeout();
                break;
            case dtlsAutoResumeTimeout:
                result= config.getDtlsAutoResumeTimeout();
                break;
            case responseMatching:
                result= ( config.getResponseMatching() != null ? config.getResponseMatching().name() : null );
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
            case midTracker:
                result= ( config.getMidTracker() != null ? config.getMidTracker().name() : null );
                break;
            case midTrackerGroups:
                result= config.getMidTrackerGroups();
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
            case maxResourceBodySize:
                result= config.getMaxResourceBodySize();
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
            case logHealthStatus:
                result= Boolean.toString( config.isLogHealthStatus() );
                break;
            case healthStatusInterval:
                result= config.getHealthStatusInterval();
                break;
            default:
                throw new Exception( "attributename unknown" );
        }
        return result;
    }

    /**
     * @return the NetworkConfig key when attribute is a NetworkConfig attribute, otherwise null is returned
     * @throws Exception when attribute is invalid
     */
    static public String getKey( AttributeName attributeName ) throws Exception
    {
        String result= null;

        switch ( attributeName )
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
            case maxActivePeers:
                result= NetworkConfig.Keys.MAX_ACTIVE_PEERS;
                break;
            case maxPeerInactivityPeriod:
                result= NetworkConfig.Keys.MAX_PEER_INACTIVITY_PERIOD;
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
            case secureSessionTimeout:
                result= NetworkConfig.Keys.SECURE_SESSION_TIMEOUT;
                break;
            case dtlsAutoResumeTimeout:
                result= NetworkConfig.Keys.DTLS_AUTO_RESUME_TIMEOUT;
                break;
            case responseMatching:
                result= NetworkConfig.Keys.RESPONSE_MATCHING;
                break;
            case useRandomMidStart:
                result= NetworkConfig.Keys.USE_RANDOM_MID_START;
                break;
            case midTracker:
                result= NetworkConfig.Keys.MID_TRACKER;
                break;
            case midTrackerGroups:
                result= NetworkConfig.Keys.MID_TRACKER_GROUPS;
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
            case maxResourceBodySize:
                result= NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE;
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
            // used by californium as attribute value
            //                case deduplicatorMarkAndSweep:
            //                    result= NetworkConfig.Keys.DEDUPLICATOR_MARK_AND_SWEEP;
            //                    break;
            case markAndSweepInterval:
                result= NetworkConfig.Keys.MARK_AND_SWEEP_INTERVAL;
                break;
            // used by californium as attribute value
            //                case deduplicatorCropRotation:
            //                    result= NetworkConfig.Keys.DEDUPLICATOR_CROP_ROTATION;
            //                    break;
            case cropRotationPeriod:
                result= NetworkConfig.Keys.CROP_ROTATION_PERIOD;
                break;
            // used by californium as attribute value
            //                case noDeduplicator:
            //                    result= NetworkConfig.Keys.NO_DEDUPLICATOR;
            //                    break;
            //case httpPort: result= NetworkConfig.Keys.httpPort ; break;
            // case httpServerSocketTimeout: result= NetworkConfig.Keys.httpServerSocketTimeout ; break;
            // case httpServerSocketBufferSize: result= NetworkConfig.Keys.httpServerSocketBufferSize ; break;
            // case httpCacheResponseMaxAge: result= NetworkConfig.Keys.httpCacheResponseMaxAge ; break;
            // case httpCacheSize: result= NetworkConfig.Keys.httpCacheSize ; break;
            case logHealthStatus:
                break;
            case healthStatusInterval:
                result= NetworkConfig.Keys.HEALTH_STATUS_INTERVAL;
                break;

            default:
                throw new Exception( "attribute name unknown" );
        }
        return result;
    }

    /**
     * @return the attribute name corresponding to NetworkConfig key
     * @throws Exception when key is not known
     */
    static public AttributeName getName( String key ) throws Exception
    {
        AttributeName result= null;

        switch ( key )
        {
            case NetworkConfig.Keys.COAP_PORT:
                result= AttributeName.bindToPort;
                break;
            case NetworkConfig.Keys.COAP_SECURE_PORT:
                result= AttributeName.bindToSecurePort;
                break;
            case NetworkConfig.Keys.MAX_ACTIVE_PEERS:
                result= AttributeName.maxActivePeers;
                break;
            case NetworkConfig.Keys.MAX_PEER_INACTIVITY_PERIOD:
                result= AttributeName.maxPeerInactivityPeriod;
                break;
            case NetworkConfig.Keys.ACK_TIMEOUT:
                result= AttributeName.ackTimeout;
                break;
            case NetworkConfig.Keys.ACK_RANDOM_FACTOR:
                result= AttributeName.ackRandomFactor;
                break;
            case NetworkConfig.Keys.ACK_TIMEOUT_SCALE:
                result= AttributeName.ackTimeoutScale;
                break;
            case NetworkConfig.Keys.MAX_RETRANSMIT:
                result= AttributeName.maxRetransmit;
                break;
            case NetworkConfig.Keys.EXCHANGE_LIFETIME:
                result= AttributeName.exchangeLifetime;
                break;
            case NetworkConfig.Keys.NON_LIFETIME:
                result= AttributeName.nonLifetime;
                break;
            case NetworkConfig.Keys.MAX_TRANSMIT_WAIT:
                result= AttributeName.maxTransmitWait;
                break;
            case NetworkConfig.Keys.NSTART:
                result= AttributeName.nstart;
                break;
            case NetworkConfig.Keys.LEISURE:
                result= AttributeName.leisure;
                break;
            case NetworkConfig.Keys.PROBING_RATE:
                result= AttributeName.probingRate;
                break;
            case NetworkConfig.Keys.USE_RANDOM_MID_START:
                result= AttributeName.useRandomMidStart;
                break;
            case NetworkConfig.Keys.MID_TRACKER:
                result= AttributeName.midTracker;
                break;
            case NetworkConfig.Keys.MID_TRACKER_GROUPS:
                result= AttributeName.midTrackerGroups;
                break;
            case NetworkConfig.Keys.TOKEN_SIZE_LIMIT:
                result= AttributeName.tokenSizeLimit;
                break;
            case NetworkConfig.Keys.PREFERRED_BLOCK_SIZE:
                result= AttributeName.preferredBlockSize;
                break;
            case NetworkConfig.Keys.MAX_MESSAGE_SIZE:
                result= AttributeName.maxMessageSize;
                break;
            case NetworkConfig.Keys.MAX_RESOURCE_BODY_SIZE:
                result= AttributeName.maxResourceBodySize;
                break;
            case NetworkConfig.Keys.BLOCKWISE_STATUS_LIFETIME:
                result= AttributeName.blockwiseStatusLifetime;
                break;
            case NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_TIME:
                result= AttributeName.notificationCheckIntervalTime;
                break;
            case NetworkConfig.Keys.NOTIFICATION_CHECK_INTERVAL_COUNT:
                result= AttributeName.notificationCheckIntervalCount;
                break;
            case NetworkConfig.Keys.NOTIFICATION_REREGISTRATION_BACKOFF:
                result= AttributeName.notificationReregistrationBackoff;
                break;
            case NetworkConfig.Keys.USE_CONGESTION_CONTROL:
                result= AttributeName.useCongestionControl;
                break;
            case NetworkConfig.Keys.CONGESTION_CONTROL_ALGORITHM:
                result= AttributeName.congestionControlAlgorithm;
                break;
            case NetworkConfig.Keys.PROTOCOL_STAGE_THREAD_COUNT:
                result= AttributeName.protocolStageThreadCount;
                break;
            case NetworkConfig.Keys.NETWORK_STAGE_RECEIVER_THREAD_COUNT:
                result= AttributeName.networkStageReceiverThreadCount;
                break;
            case NetworkConfig.Keys.NETWORK_STAGE_SENDER_THREAD_COUNT:
                result= AttributeName.networkStageSenderThreadCount;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_DATAGRAM_SIZE:
                result= AttributeName.udpConnectorDatagramSize;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_RECEIVE_BUFFER:
                result= AttributeName.udpConnectorReceiveBuffer;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_SEND_BUFFER:
                result= AttributeName.udpConnectorSendBuffer;
                break;
            case NetworkConfig.Keys.UDP_CONNECTOR_OUT_CAPACITY:
                result= AttributeName.udpConnectorOutCapacity;
                break;
            case NetworkConfig.Keys.DEDUPLICATOR:
                result= AttributeName.deduplicator;
                break;
            // used by californium as attribute value
            //                case : result= deduplicatorMarkAndSweep:
            //                     NetworkConfig.Keys.DEDUPLICATOR_MARK_AND_SWEEP;
            //                    break;
            case NetworkConfig.Keys.MARK_AND_SWEEP_INTERVAL:
                result= AttributeName.markAndSweepInterval;
                break;
            // used by californium as attribute value
            //                case : result= deduplicatorCropRotation:
            //                     NetworkConfig.Keys.DEDUPLICATOR_CROP_ROTATION;
            //                    break;
            case NetworkConfig.Keys.CROP_ROTATION_PERIOD:
                result= AttributeName.cropRotationPeriod;
                break;
            // used by californium as attribute value
            //                case : result= noDeduplicator:
            //                     NetworkConfig.Keys.NO_DEDUPLICATOR;
            //                    break;
            //case : result= httpPort:  NetworkConfig.Keys.httpPort ; break;
            // case : result= httpServerSocketTimeout:  NetworkConfig.Keys.httpServerSocketTimeout ; break;
            // case : result= httpServerSocketBufferSize:  NetworkConfig.Keys.httpServerSocketBufferSize ; break;
            // case : result= httpCacheResponseMaxAge:  NetworkConfig.Keys.httpCacheResponseMaxAge ; break;
            // case : result= httpCacheSize:  NetworkConfig.Keys.httpCacheSize ; break;
            case NetworkConfig.Keys.HEALTH_STATUS_INTERVAL:
                result= AttributeName.healthStatusInterval;
                break;
            case NetworkConfig.Keys.SECURE_SESSION_TIMEOUT:
                result= AttributeName.secureSessionTimeout;
                break;
            case NetworkConfig.Keys.DTLS_AUTO_RESUME_TIMEOUT:
                result= AttributeName.dtlsAutoResumeTimeout;
                break;
            case NetworkConfig.Keys.RESPONSE_MATCHING:
                result= AttributeName.responseMatching;
                break;
            default:
                throw new Exception( "key unknown" );
        }
        return result;
    }

    /**
     * Establish whether the attribute is a NetworkConfig attribute 
     * @param AttributeName the attribute name
     * @throws Exception when invalid name
     */
    static public boolean isNetworkConfig( AttributeName attributeName ) throws Exception
    {
        return( getKey( attributeName ) != null );
    }

    /**
     * Get the value of a NetworkConfig attribute
     * @return the actual value when it is a NetworkConfig attribute, otherwise null
     * @throws Exception when name is unknown
     */
    static public String getNetworkConfigValue( AttributeName attributeName, ServerConfig config ) throws Exception
    {
        String key= getKey( attributeName );
        String result= null;

        if ( key != null )
        {
            result= config.getNetworkConfig().getString( key );
        }
        return result;
    }

    /**
     * Set value for the attribute
     * @param config configuration to set the attribute on
     * @param value to set
     * @throws Exception when attribute is invalid
     */
    static public void setValue( AttributeName attributeName, ServerConfig config, String value ) throws Exception
    {
        switch ( attributeName )
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
            case maxActivePeers:
                config.setMaxActivePeers( value );
                break;
            case maxPeerInactivityPeriod:
                config.setMaxPeerInactivityPeriod( value );
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
            case secureSessionTimeout:
                config.setSecureSessionTimeout( value );
                break;
            case dtlsAutoResumeTimeout:
                config.setDtlsAutoResumeTimeout( value );
                break;
            case responseMatching:
                config.setResponseMatching( DtlsResponseMatchingName.valueOf( value ) );
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
            case midTracker:
                config.setMidTracker( MidTrackerName.valueOf( value ) );
                break;
            case midTrackerGroups:
                config.setMidTrackerGroups( value );
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
            case maxResourceBodySize:
                config.setMaxResourceBodySize( value );
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
            case logHealthStatus:
                config.setLogHealthStatus( Boolean.parseBoolean( value ) );
                break;
            case healthStatusInterval:
                config.setLogHealthStatus( true );
                config.setHealthStatusInterval( value );
                break;
            default:
                throw new Exception( "attribute name unknown" );
        }
    }
}