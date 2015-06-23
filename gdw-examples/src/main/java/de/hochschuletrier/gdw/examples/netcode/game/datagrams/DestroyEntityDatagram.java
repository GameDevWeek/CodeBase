package de.hochschuletrier.gdw.examples.netcode.game.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 *
 * @author Santo Pfingsten
 */
public class DestroyEntityDatagram extends NetDatagram {
    protected long id;

    public static DestroyEntityDatagram create(long id) {
        DestroyEntityDatagram datagram = DatagramFactory.create(DestroyEntityDatagram.class);
        datagram.id = id;
        return datagram;
    }

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(id);
    }

    @Override
    public void readFromMessage(NetMessageIn message) {
        id = message.getLong();
    }
}
