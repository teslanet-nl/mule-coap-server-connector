package nl.teslanet.mule.transport.coap.server.test.modules;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.californium.core.network.config.NetworkConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.server.config.EndpointConfig;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;


public class EndpointConfigTest
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
        EndpointConfig config= new EndpointConfig();
        assertNotNull( "constructor deliverd null", config );
    }

    @Test
    public void testGettersAndSetters() throws ResourceUriException
    {
        EndpointConfig config= new EndpointConfig();

        String bindToHost= "bindToHost";
        String bindToPort= "bindToPort";
        String bindToSecurePort= "bindToSecurePort";
        String ackTimeout= "ackTimeout";
        String ackRandomFactor= "ackRandomFactor";
        String ackTimeoutScale= "ackTimeoutScale";
        String maxRetransmit= "maxRetransmit";
        String exchangeLifetime= "exchangeLifetime";
        String nonLifetime= "nonLifetime";
        String maxTransmitWait= "maxTransmitWait";
        String nstart= "nstart";
        String leisure= "leisure";
        String probingRate= "probingRate";
        String keyStoreLocation= "keyStoreLocation";
        String keyStorePassword= "keyStorePassword";
        String privateKeyAlias= "privateKeyAlias";
        String trustStoreLocation= "trustStoreLocation";
        String trustStorePassword= "trustStorePassword";
        String trustedRootCertificateAlias= "trustedRootCertificateAlias";
        String useRandomMidStart= "useRandomMidStart";
        String tokenSizeLimit= "tokenSizeLimit";
        String preferredBlockSize= "preferredBlockSize";
        String maxMessageSize= "maxMessageSize";
        String blockwiseStatusLifetime= "blockwiseStatusLifetime";
        String notificationCheckIntervalTime= "notificationCheckIntervalTime";
        String notificationCheckIntervalCount= "notificationCheckIntervalCount";
        String notificationReregistrationBackoff= "notificationReregistrationBackoff";
        String useCongestionControl= "useCongestionControl";
        String congestionControlAlgorithm= "congestionControlAlgorithm";
        String protocolStageThreadCount= "protocolStageThreadCount";
        String networkStageReceiverThreadCount= "networkStageReceiverThreadCount";
        String networkStageSenderThreadCount= "networkStageSenderThreadCount";
        String udpConnectorDatagramSize= "udpConnectorDatagramSize";
        String udpConnectorReceiveBuffer= "udpConnectorReceiveBuffer";
        String udpConnectorSendBuffer= "udpConnectorSendBuffer";
        String udpConnectorOutCapacity= "udpConnectorOutCapacity";
        String deduplicator= "deduplicator";
        String deduplicatorMarkAndSweep= "deduplicatorMarkAndSweep";
        String markAndSweepInterval= "markAndSweepInterval";
        String deduplicatorCropRotation= "deduplicatorCropRotation";
        String cropRotationPeriod= "cropRotationPeriod";
        String noDeduplicator= "noDeduplicator";
        //String httpPort= "httpPort";
        // String httpServerSocketTimeout= "httpServerSocketTimeout";
        // String httpServerSocketBufferSize= "httpServerSocketBufferSize";
        // String httpCacheResponseMaxAge= "httpCacheResponseMaxAge";
        // String httpCacheSize= "httpCacheSize";
        String healthStatusInterval= "healthStatusInterval";

        String input;
        String output;

        input= bindToHost;
        config.setBindToHost( input );
        output= config.getBindToHost();
        assertEquals( "get/set failed on " + input, input, output );

        input= bindToPort;
        config.setBindToPort( input );
        output= config.getBindToPort();
        assertEquals( "get/set failed on " + input, input, output );

        input= bindToPort;
        config.setBindToPort( input );
        output= config.getBindToPort();
        assertEquals( "get/set failed on " + input, input, output );

        input= bindToSecurePort;
        config.setBindToSecurePort( input );
        output= config.getBindToSecurePort();
        assertEquals( "get/set failed on " + input, input, output );

        input= keyStoreLocation;
        config.setKeyStoreLocation( input );
        output= config.getKeyStoreLocation();
        assertEquals( "get/set failed on " + input, input, output );

        input= keyStorePassword;
        config.setKeyStorePassword( input );
        output= config.getKeyStorePassword();
        assertEquals( "get/set failed on " + input, input, output );

        input= trustStoreLocation;
        config.setTrustStoreLocation( input );
        output= config.getTrustStoreLocation();
        assertEquals( "get/set failed on " + input, input, output );

        input= trustStorePassword;
        config.setTrustStorePassword( input );
        output= config.getTrustStorePassword();
        assertEquals( "get/set failed on " + input, input, output );

        input= privateKeyAlias;
        config.setPrivateKeyAlias( input );
        output= config.getPrivateKeyAlias();
        assertEquals( "get/set failed on " + input, input, output );

        input= trustedRootCertificateAlias;
        config.setTrustedRootCertificateAlias( input );
        output= config.getTrustedRootCertificateAlias();
        assertEquals( "get/set failed on " + input, input, output );

        input= ackTimeout;
        config.setAckTimeout( input );
        output= config.getAckTimeout();
        assertEquals( "get/set failed on " + input, input, output );

        input= ackRandomFactor;
        config.setAckRandomFactor( input );
        output= config.getAckRandomFactor();
        assertEquals( "get/set failed on " + input, input, output );

        input= ackTimeoutScale;
        config.setAckTimeoutScale( input );
        output= config.getAckTimeoutScale();
        assertEquals( "get/set failed on " + input, input, output );

        input= maxRetransmit;
        config.setMaxRetransmit( input );
        output= config.getMaxRetransmit();
        assertEquals( "get/set failed on " + input, input, output );

        input= exchangeLifetime;
        config.setExchangeLifetime( input );
        output= config.getExchangeLifetime();
        assertEquals( "get/set failed on " + input, input, output );

        input= nonLifetime;
        config.setNonLifetime( input );
        output= config.getNonLifetime();
        assertEquals( "get/set failed on " + input, input, output );

        input= maxTransmitWait;
        config.setMaxTransmitWait( input );
        output= config.getMaxTransmitWait();
        assertEquals( "get/set failed on " + input, input, output );

        input= maxTransmitWait;
        config.setNstart( input );
        output= config.getNstart();
        assertEquals( "get/set failed on " + input, input, output );

        input= leisure;
        config.setLeisure( input );
        output= config.getLeisure();
        assertEquals( "get/set failed on " + input, input, output );

        input= probingRate;
        config.setProbingRate( input );
        output= config.getProbingRate();
        assertEquals( "get/set failed on " + input, input, output );

        input= useRandomMidStart;
        config.setUseRandomMidStart( input );
        output= config.getUseRandomMidStart();
        assertEquals( "get/set failed on " + input, input, output );

        input= tokenSizeLimit;
        config.setTokenSizeLimit( input );
        output= config.getTokenSizeLimit();
        assertEquals( "get/set failed on " + input, input, output );

        input= preferredBlockSize;
        config.setPreferredBlockSize( input );
        output= config.getPreferredBlockSize();
        assertEquals( "get/set failed on " + input, input, output );

        input= maxMessageSize;
        config.setMaxMessageSize( input );
        output= config.getMaxMessageSize();
        assertEquals( "get/set failed on " + input, input, output );

        input= blockwiseStatusLifetime;
        config.setBlockwiseStatusLifetime( input );
        output= config.getBlockwiseStatusLifetime();
        assertEquals( "get/set failed on " + input, input, output );

        input= notificationCheckIntervalTime;
        config.setNotificationCheckIntervalTime( input );
        output= config.getNotificationCheckIntervalTime();
        assertEquals( "get/set failed on " + input, input, output );

        input= notificationCheckIntervalCount;
        config.setNotificationCheckIntervalCount( input );
        output= config.getNotificationCheckIntervalCount();
        assertEquals( "get/set failed on " + input, input, output );

        input= notificationReregistrationBackoff;
        config.setNotificationReregistrationBackoff( input );
        output= config.getNotificationReregistrationBackoff();
        assertEquals( "get/set failed on " + input, input, output );

        input= useCongestionControl;
        config.setUseCongestionControl( input );
        output= config.getUseCongestionControl();
        assertEquals( "get/set failed on " + input, input, output );

        input= congestionControlAlgorithm;
        config.setCongestionControlAlgorithm( input );
        output= config.getCongestionControlAlgorithm();
        assertEquals( "get/set failed on " + input, input, output );

        input= protocolStageThreadCount;
        config.setProtocolStageThreadCount( input );
        output= config.getProtocolStageThreadCount();
        assertEquals( "get/set failed on " + input, input, output );

        input= networkStageReceiverThreadCount;
        config.setNetworkStageReceiverThreadCount( input );
        output= config.getNetworkStageReceiverThreadCount();
        assertEquals( "get/set failed on " + input, input, output );

        input= networkStageSenderThreadCount;
        config.setNetworkStageSenderThreadCount( input );
        output= config.getNetworkStageSenderThreadCount();
        assertEquals( "get/set failed on " + input, input, output );

        input= udpConnectorDatagramSize;
        config.setUdpConnectorDatagramSize( input );
        output= config.getUdpConnectorDatagramSize();
        assertEquals( "get/set failed on " + input, input, output );

        input= udpConnectorReceiveBuffer;
        config.setUdpConnectorReceiveBuffer( input );
        output= config.getUdpConnectorReceiveBuffer();
        assertEquals( "get/set failed on " + input, input, output );

        input= udpConnectorSendBuffer;
        config.setUdpConnectorSendBuffer( input );
        output= config.getUdpConnectorSendBuffer();
        assertEquals( "get/set failed on " + input, input, output );

        input= udpConnectorOutCapacity;
        config.setUdpConnectorOutCapacity( input );
        output= config.getUdpConnectorOutCapacity();
        assertEquals( "get/set failed on " + input, input, output );

        input= deduplicator;
        config.setDeduplicator( input );
        output= config.getDeduplicator();
        assertEquals( "get/set failed on " + input, input, output );

        input= deduplicatorMarkAndSweep;
        config.setDeduplicatorMarkAndSweep( input );
        output= config.getDeduplicatorMarkAndSweep();
        assertEquals( "get/set failed on " + input, input, output );

        input= markAndSweepInterval;
        config.setMarkAndSweepInterval( input );
        output= config.getMarkAndSweepInterval();
        assertEquals( "get/set failed on " + input, input, output );

        input= deduplicatorCropRotation;
        config.setDeduplicatorCropRotation( input );
        output= config.getDeduplicatorCropRotation();
        assertEquals( "get/set failed on " + input, input, output );

        input= cropRotationPeriod;
        config.setCropRotationPeriod( input );
        output= config.getCropRotationPeriod();
        assertEquals( "get/set failed on " + input, input, output );

        input= noDeduplicator;
        config.setNoDeduplicator( input );
        output= config.getNoDeduplicator();
        assertEquals( "get/set failed on " + input, input, output );

        input= healthStatusInterval;
        config.setHealthStatusInterval( input );
        output= config.getHealthStatusInterval();
        assertEquals( "get/set failed on " + input, input, output );

    }

    @Test
    public void testGetNetworkConfigEmpty() throws ResourceUriException
    {
        EndpointConfig config= new EndpointConfig();     
        NetworkConfig networkConfig= config.getNetworkConfig();
        
        assertNotNull( networkConfig );
        NetworkConfig stdNetworkConfig= NetworkConfig.createStandardWithoutFile();
        stdNetworkConfig.setStandard( networkConfig );
    }

}
