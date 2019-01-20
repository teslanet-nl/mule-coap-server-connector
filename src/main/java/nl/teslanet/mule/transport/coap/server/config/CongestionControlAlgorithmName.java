package nl.teslanet.mule.transport.coap.server.config;

/**
 * The designators for the available types of congestion control.
 *
 */
public enum CongestionControlAlgorithmName
{
    Cocoa, 
    CocoaStrong,
    BasicRto,
    LinuxRto,
    PeakhopperRto
}
