package de.hochschuletrier.gdw.examples.netcode.pingpong.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageOut;
import de.hochschuletrier.gdw.commons.netcode.core.NetMessageType;

/**
 *
 * @author Santo Pfingsten
 */
public class ChatDatagram extends NetDatagram {

    private String text;
    private long timestamp;

    public static ChatDatagram create(String text) {
        ChatDatagram datagram = DatagramFactory.create(ChatDatagram.class);
        datagram.text = text;
        datagram.timestamp = System.currentTimeMillis();
        return datagram;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void reset() {
        super.reset();

        text = null;
        timestamp = 0;
    }

    @Override
    public NetMessageType getMessageType() {
        return NetMessageType.NORMAL;
    }

    @Override
    public void writeToMessage(NetMessageOut message) {
        message.putString(text);
        message.putLong(timestamp);
    }

    @Override
    public void readFromMessage(NetMessageIn message) {
        text = message.getString();
        timestamp = message.getLong();
    }
}
