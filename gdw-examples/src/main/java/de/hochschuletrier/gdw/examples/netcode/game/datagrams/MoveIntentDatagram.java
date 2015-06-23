package de.hochschuletrier.gdw.examples.netcode.game.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;

/**
 *
 * @author Santo Pfingsten
 */
public class MoveIntentDatagram extends NetDatagram {

    private int x, y;

    public static MoveIntentDatagram create(int x, int y) {
        MoveIntentDatagram datagram = DatagramFactory.create(MoveIntentDatagram.class);
        datagram.x = x;
        datagram.y = y;
        return datagram;
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
        message.putInt(x);
        message.putInt(y);
    }

    @Override
    public void readFromMessage(NetMessageIn message) {
        x = message.getInt();
        y = message.getInt();
    }
}
