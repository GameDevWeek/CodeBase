package de.hochschuletrier.gdw.commons.netcode.core;

/**
 * Status of a network connection
 *
 * @author Santo Pfingsten
 */
public enum NetStatus {

    INITIAL,
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    TIMED_OUT;
}
