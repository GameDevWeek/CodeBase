package de.hochschuletrier.gdw.commons.netcode.core;

/**
 * An outgoing message
 *
 * @author Santo Pfingsten
 */
public interface NetMessageOut {

    /**
     * @return the maximum capacity of the buffer (not important while reading)
     */
    int capacity();

    /**
     * @return how many bytes have been read so far
     */
    int position();

    /**
     * @return how many bytes are left to read
     */
    int remaining();

    /**
     * @return the amount of stored bytes in this buffer
     */
    int limit();

    /**
     * Write a byte into the buffer
     *
     * @param value the value
     */
    void put(byte value);

    /**
     * Write a boolean value (stored as byte) into the buffer
     *
     * @param value the value
     */
    void putBool(boolean value);

    /**
     * Write a character into the buffer
     *
     * @param value the value
     */
    void putChar(char value);

    /**
     * Write a short into the buffer
     *
     * @param value the value
     */
    void putShort(short value);

    /**
     * Write an integer into the buffer
     *
     * @param value the value
     */
    void putInt(int value);

    /**
     * Write a long into the buffer
     *
     * @param value the value
     */
    void putLong(long value);

    /**
     * Write a float into the buffer
     *
     * @param value the value
     */
    void putFloat(float value);

    /**
     * Write a double into the buffer
     *
     * @param value the value
     */
    void putDouble(double value);

    /**
     * Write an enum into the buffer
     *
     * @param value the value
     */
    void putEnum(Enum value);

    /**
     * Write a string into the buffer (a number of characters terminated by a '\0')
     *
     * @param value the value
     */
    void putString(String value);

    /**
     * Write a string into the buffer (a number of characters terminated by a '\0').
     * Cuts of anything longer than maxLength
     *
     * @param value the value
     * @param maxLength the maximum length of the string
     */
    void putString(String value, int maxLength);
}
