package de.hochschuletrier.gdw.commons.netcode.core;

import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A NetConnection represents a connection from server to client or vice versa.
 * This connection is capable of sending and receiving datagrams via tcp (reliable) and udp (unreliable).
 *
 * @author Santo Pfingsten
 */
public class NetConnection {

    private static final Logger logger = LoggerFactory.getLogger(NetConnection.class);

    private Object attachment;
    protected final SocketChannel tcpChannel;
    protected final DatagramChannel udpChannel;
    protected final SocketAddress destination;
    protected final NetDatagramPool datagramPool;
    protected volatile NetStatus status = NetStatus.INITIAL;
    private IOException disconnectException;

    private final ConcurrentLinkedQueue<NetDatagram> inDatagrams = new ConcurrentLinkedQueue();
    private final LinkedList<NetDatagram> outDatagramsReliable = new LinkedList();

    protected final NetStatistic udpStatistic = new NetStatistic();
    protected final NetStatistic tcpStatistic = new NetStatistic();
    private final NetManager manager;

    NetConnection(SocketChannel tcpChannel, DatagramChannel udpChannel, NetDatagramPool datagramPool, NetManager manager) throws IOException {
        this.udpChannel = udpChannel;
        this.destination = tcpChannel.getRemoteAddress();
        this.tcpChannel = tcpChannel;
        this.manager = manager;

        this.datagramPool = datagramPool;
        status = NetStatus.CONNECTING;
    }

    /**
     * @return the attachment previously attached to this connection
     */
    public Object getAttachment() {
        return attachment;
    }

    /**
     * Set an attachment to this connection.
     * This can be helpful to (for example) store the player reference bound to this connection.
     *
     * @param object the attachment object
     */
    public void setAttachment(Object object) {
        attachment = object;
    }

    /**
     * @return  the statistics for the UDP connection
     */
    public NetStatistic getUdpStatistic() {
        return udpStatistic;
    }

    /**
     * @return  the statistics for the TCP connection
     */
    public NetStatistic getTcpStatistic() {
        return tcpStatistic;
    }

    /**
     * @return the connection status
     */
    public NetStatus getStatus() {
        return status;
    }

    /**
     * @return true if the connection is still established
     */
    public boolean isConnected() {
        return status.ordinal() <= NetStatus.CONNECTED.ordinal() && tcpChannel.isConnected();
    }

    /**
     * Shut down the connection
     */
    public void disconnect() {
        status = NetStatus.DISCONNECTED;
        try {
            tcpChannel.close();
        } catch (IOException e) {
            logger.error("Failed to close tcp channel", e);
        }

        // Notify the manager about the disconnect
        manager.onDisconnect(this);
    }

    /**
     * @return true if datagrams can be received
     */
    public boolean hasIncoming() {
        return !inDatagrams.isEmpty();
    }

    /**
     * @return the next datagram available
     */
    public NetDatagram receive() {
        return inDatagrams.poll();
    }

    /**
     * Send a datagram to this connection, but don't care if it arrives nor not.
     * The datagram will be freed when it has been send to all connections
     *
     * @param datagram the datagram to be send
     */
    public void sendUnreliable(NetDatagram datagram) {
        if (datagram.sequenceId == 0) {
            datagram.sequenceId = NetConfig.nextSequenceId();
        }
        manager.sendUnreliable(datagram, this);
    }

    /**
     * Send a datagram to this connection and make sure it arrives.
     * The datagram will be freed when it has been send to all connections
     *
     * @param datagram the datagram to be send
     */
    public void sendReliable(NetDatagram datagram) {
        if (datagram.sequenceId == 0) {
            datagram.sequenceId = NetConfig.nextSequenceId();
        }
        synchronized (outDatagramsReliable) {
            outDatagramsReliable.add(datagram);
            manager.onDatagramQueueUpdate(tcpChannel, false);
        }
    }

    protected NetDatagram pollOutDatagramReliable() {
        synchronized (outDatagramsReliable) {
            NetDatagram datagram = outDatagramsReliable.poll();
            if (datagram == null) {
                manager.onDatagramQueueUpdate(tcpChannel, true);
            }
            return datagram;
        }
    }

    protected void handleInternalDatagram(NetDatagramType type, NetDatagram datagram) {
        switch (type) {
            case CONNECT:
                status = NetStatus.CONNECTED; // fixme: if is client print warning instead
                break;
            case DISCONNECT:
                disconnect();
                break;
            case KEEP_ALIVE:
                // nothing to do
                break;
            default:
                logger.error("Bad internal message received: {}", type);
                break;
        }
    }

    protected void onDatagramReceived(NetDatagram datagram) {
        if (datagram.type < NetDatagramType.FIRST_CUSTOM.toID()) {
            try {
                handleInternalDatagram(NetDatagramType.fromID(datagram.type), datagram);
            } finally {
                datagramPool.free(datagram);
            }
        } else {
            inDatagrams.add(datagram);
        }
    }

    /**
     * @return the exception that caused a disconnect
     */
    public IOException getDisconnectException() {
        return disconnectException;
    }

    /**
     * Set the time when to send a keep alive signal.
     *
     * @param ms the time in ms since the last message has been send.
     */
    public void setKeepAlive(int ms) {
    }

    NetManager.Listener tcpListener = new NetManager.Listener() {
        private final NetDatagramReader datagramReader = new NetDatagramReader();
        private final NetDatagramWriter datagramWriter = new NetDatagramWriter();

        @Override
        public void onChannelWritable(SelectableChannel channel) {
            try {
                datagramWriter.writeToConnection(NetConnection.this);
            } catch (Exception e) {
                logger.error("error writing data to tcp", e);
                disconnect();
            }
        }

        @Override
        public void onChannelReadable(SelectableChannel channel) {
            try {
                datagramReader.readFromConnection(NetConnection.this);
            } catch (Exception e) {
                logger.error("error reading data from tcp", e);
                disconnect();
            }
        }
    };
}
