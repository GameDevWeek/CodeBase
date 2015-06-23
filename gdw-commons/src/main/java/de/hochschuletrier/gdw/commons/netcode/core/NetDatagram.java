package de.hochschuletrier.gdw.commons.netcode.core;

import de.hochschuletrier.gdw.commons.utils.pool.Poolable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The most basic datagram
 *
 * @author Santo Pfingsten
 */
public class NetDatagram implements Poolable {

    private final AtomicInteger broadcastCount = new AtomicInteger(1);
    NetConnection connection;

    protected int messageSize;
    protected long sequenceId;
    protected short type;

    public final void init(short type) {
        this.type = type;
    }

    @Override
    public void reset() {
        broadcastCount.set(1);
        messageSize = 0;
        sequenceId = 0;
        type = 0;
    }

    public void setBroadcastCount(int broadcastCount) {
        this.broadcastCount.set(broadcastCount);
    }

    public final boolean onSendComplete() {
        return broadcastCount.decrementAndGet() == 0;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public long getSequenceId() {
        return sequenceId;
    }

    public short getType() {
        return type;
    }

    public final NetConnection getConnection() {
        return connection;
    }

    /**
     * @return The type of message this datagram contains
     */
    public NetMessageType getMessageType() {
        return NetMessageType.NONE;
    }

    /**
     * Write all data to the message
     *
     * @param message the message to be send
     */
    public void writeToMessage(NetMessageOut message) {
    }

    /**
     * Read all data from the message
     *
     * @param message the message to read from
     */
    public void readFromMessage(NetMessageIn message) {
        
    }
}
