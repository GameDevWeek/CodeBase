package de.hochschuletrier.gdw.commons.netcode.core;

/**
 * Some constants and some variables the library user can change
 *
 * @author Santo Pfingsten
 */
public class NetConfig {

    /** The header size.. can't modify this. */
    public static final int HEADER_SIZE = 4 + 8 + 2;

    /**
     * The maximum number of bytes a message can take.
     * It is advised to keep the datagram size below 1500 to avoid router fragmentation
     */
    public static int MAX_MESSAGE_SIZE = 1400;
    /** The max. number of bytes a UTF8 String may contain */
    public static short MAX_STRING_BYTES = 1024;

    /** @return The maximum number of bytes a datagram packet can take */
    public static int getMaxDatagramSize() {
        return MAX_MESSAGE_SIZE + HEADER_SIZE;
    }

    private static long nextSequenceId = 1;

    static long nextSequenceId() {
        return nextSequenceId++;
    }
}
