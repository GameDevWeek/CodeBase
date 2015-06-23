package de.hochschuletrier.gdw.commons.netcode.core;

/**
 * A datagram type interface
 *
 * @author Santo Pfingsten
 */
public enum NetDatagramType {

    /** Client is trying to connect */
    CONNECT,
    /** Client or server wants to disconnect */
    DISCONNECT,
    /** Send to keep the connection alive */
    KEEP_ALIVE,
    /** The first custom type */
    FIRST_CUSTOM;

    public short toID() {
        return (short) ordinal();
    }

    public static NetDatagramType fromID(short id) {
        assert(id >= 0 && id < FIRST_CUSTOM.ordinal());
        return values()[id];
    }
}
