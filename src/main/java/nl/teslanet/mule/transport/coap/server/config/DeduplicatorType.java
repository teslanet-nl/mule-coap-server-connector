package nl.teslanet.mule.transport.coap.server.config;

/**
 * The designator for the type of deduplication that is used
 *
 */
public enum DeduplicatorType
{
    //TODO: make independent of californium values

    NO_DEDUPLICATOR, DEDUPLICATOR_MARK_AND_SWEEP, DEDUPLICATOR_CROP_ROTATION
}
