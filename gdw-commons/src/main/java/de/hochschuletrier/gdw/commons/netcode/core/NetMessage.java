package de.hochschuletrier.gdw.commons.netcode.core;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A normal (uncompressed) message
 *
 * @author Santo Pfingsten
 */
public class NetMessage implements NetMessageIn, NetMessageOut {

    private static final Logger logger = LoggerFactory.getLogger(NetMessage.class);

    final ByteBuffer buffer = ByteBuffer.allocate(NetConfig.getMaxDatagramSize());
    private final byte[] stringBytes = new byte[NetConfig.MAX_STRING_BYTES];

    void writeToBuffer(NetDatagram datagram) {
        putInt(0); // placeholder for messageSize
        putLong(datagram.sequenceId);
        putShort(datagram.type);
        if (datagram.getMessageType() != NetMessageType.NONE) {
            datagram.writeToMessage(this);
            datagram.messageSize = buffer.position() - NetConfig.HEADER_SIZE;
            buffer.flip();
            buffer.putInt(0, datagram.messageSize);
        }
    }

    void readDatagram(NetConnection connection) {
        connection.udpStatistic.numBytesRead += buffer.limit();
        connection.udpStatistic.numDatagramsReceived++;

        buffer.rewind();
        NetDatagram datagram = null;
        try {
            int messageSize = getInt();
            long sequenceId = getLong();
            short type = getShort();
            datagram = connection.datagramPool.obtain(type);
            datagram.messageSize = messageSize;
            datagram.sequenceId = sequenceId;
            datagram.connection = connection;
            switch (datagram.getMessageType()) {
                case NORMAL:
                    // Let the datagram read its data
                    datagram.readFromMessage(this);
                    break;
            }
            connection.onDatagramReceived(datagram);
        } catch (IllegalArgumentException | BufferUnderflowException e) {
            logger.error("Error reading datagram", e);
            if (datagram != null) {
                connection.datagramPool.free(datagram);
            }
        }
    }

    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public int position() {
        return buffer.position();
    }

    @Override
    public int remaining() {
        return buffer.remaining();
    }

    @Override
    public int limit() {
        return buffer.limit();
    }

    @Override
    public byte get() {
        return buffer.get();
    }

    @Override
    public boolean getBool() {
        return buffer.get() != 0;
    }

    @Override
    public char getChar() {
        return buffer.getChar();
    }

    @Override
    public short getShort() {
        return buffer.getShort();
    }

    @Override
    public int getInt() {
        return buffer.getInt();
    }

    @Override
    public long getLong() {
        return buffer.getLong();
    }

    @Override
    public float getFloat() {
        return buffer.getFloat();
    }

    @Override
    public double getDouble() {
        return buffer.getDouble();
    }

    @Override
    public <T> T getEnum(Class<T> clazz) {
        int index = getInt();
        return index < 0 ? null : clazz.getEnumConstants()[index];
    }

    @Override
    public String getString() {
        short length = buffer.getShort();
        buffer.get(stringBytes, 0, length);
        return new String(stringBytes, 0, length, StandardCharsets.UTF_8);
    }

    @Override
    public void put(byte value) {
        buffer.put(value);
    }

    @Override
    public void putBool(boolean value) {
        buffer.put((byte) (value ? 1 : 0));
    }

    @Override
    public void putChar(char value) {
        buffer.putChar(value);
    }

    @Override
    public void putShort(short value) {
        buffer.putShort(value);
    }

    @Override
    public void putInt(int value) {
        buffer.putInt(value);
    }

    @Override
    public void putLong(long value) {
        buffer.putLong(value);
    }

    @Override
    public void putFloat(float value) {
        buffer.putFloat(value);
    }

    @Override
    public void putDouble(double value) {
        buffer.putDouble(value);
    }

    @Override
    public void putEnum(Enum value) {
        putInt(value != null ? value.ordinal() : -1);
    }

    @Override
    public void putString(String value) {
        putString(value, value.length());
    }

    @Override
    public void putString(String value, int maxLength) {
        if (value.length() > maxLength) {
            value = value.substring(0, maxLength);
        }
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        assert (bytes.length <= NetConfig.MAX_STRING_BYTES);
        buffer.putShort((short) bytes.length);
        buffer.put(bytes);
    }
}
