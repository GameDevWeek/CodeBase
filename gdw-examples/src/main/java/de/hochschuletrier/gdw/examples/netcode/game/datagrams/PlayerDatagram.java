package de.hochschuletrier.gdw.examples.netcode.game.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;

/**
 *
 * @author Santo Pfingsten
 */
public class PlayerDatagram extends NetDatagram {

    protected long id;
    private int x, y;

    public static PlayerDatagram create(long id, int x, int y) {
        PlayerDatagram datagram = DatagramFactory.create(PlayerDatagram.class);
        datagram.id = id;
        datagram.x = x;
        datagram.y = y;
        return datagram;
    }

    public long getID() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putLong(id);
        message.putInt(x);
        message.putInt(y);
    }

    @Override
    public void readFromMessage(NetMessageIn message) {
        id = message.getLong();
        x = message.getInt();
        y = message.getInt();
    }
}
