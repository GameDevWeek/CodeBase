package de.hochschuletrier.gdw.commons.netcode.core;

/**
 * The available message types
 *
 * @author Santo Pfingsten
 */
public enum NetMessageType {

    /** The datagram only has a header (and 2 int parameters), no message */
    NONE,
    /** The datagram has a normal message */
    NORMAL,
}
