package de.hochschuletrier.gdw.commons.netcode.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager class for a server
 *
 * @author Santo Pfingsten
 */
public class NetManagerServer extends NetManager {

    private static final Logger logger = LoggerFactory.getLogger(NetManagerServer.class);

    /** The maximum number of clients able to connect */
    private final int maxConnections;
    /** The accepting socket channel */
    private final ServerSocketChannel tcpChannel;
    /** A list of all new connections established that have not yet been taken by the application */
    private final ConcurrentLinkedQueue<NetConnection> newConnections = new ConcurrentLinkedQueue<>();
    /** The full list of all running connections */
    private final ConcurrentLinkedQueue<NetConnection> connections = new ConcurrentLinkedQueue<>();
    /** The datagram pool to use */
    private final NetDatagramPool datagramPool;

    /**
     * Create a server on the specified ip/port accepting a maximum number of connections.
     *
     * @param port the listening port
     * @param maxConnections the maximum number of connections to accept
     * @param datagramPool the datagram pool to use
     * @throws IOException
     */
    public NetManagerServer(int port, int maxConnections, NetDatagramPool datagramPool) throws IOException {
        super(datagramPool);
        this.maxConnections = maxConnections;
        this.datagramPool = datagramPool;

        tcpChannel = ServerSocketChannel.open();
        final InetSocketAddress addr = new InetSocketAddress(port);
        tcpChannel.bind(addr);
        tcpChannel.configureBlocking(false);

        udpChannel.bind(addr);

        register(tcpChannel, SelectionKey.OP_ACCEPT, tcpListener);
    }

    /**
     * Shut down the server
     */
    @Override
    public void shutdown() {
        closeChannel();
        selector.wakeup();
    }

    private void closeChannel() {
        try {
            tcpChannel.close();
        } catch (IOException e) {
            logger.error("Failed closing channel", e);
        }
    }

    @Override
    public void onShutdown() {
        if (tcpChannel.isOpen()) {
            closeChannel();
        }

        NetConnection connection;
        while ((connection = connections.poll()) != null) {
            connection.disconnect();
        }
        while ((connection = newConnections.poll()) != null) {
            connection.disconnect();
        }
    }

    @Override
    void onDisconnect(NetConnection connection) {
        connections.remove(connection);
        connection.tcpChannel.keyFor(selector).cancel();
    }

    public boolean hasNewConnections() {
        return !newConnections.isEmpty();
    }

    public NetConnection pollNewConnection() {
        return newConnections.poll();
    }

    @Override
    public boolean isRunning() {
        return tcpChannel.isOpen();
    }

    @Override
    protected void readDatagram(SocketAddress from, NetMessage message) {
        for (NetConnection connection : connections) {
            if (from.equals(connection.destination)) {
                message.readDatagram(connection);
                break;
            }
        }
    }

    private final NetManager.Listener tcpListener = new NetManager.Listener() {
        @Override
        public void onChannelAcceptable(SelectableChannel channel) {
            try {
                SocketChannel ch = tcpChannel.accept();
                if (ch != null) {
                    if (connections.size() < maxConnections) {
                        ch.configureBlocking(false);
                        ch.setOption(StandardSocketOptions.TCP_NODELAY, true);
                        NetConnection client = new NetConnection(ch, udpChannel, datagramPool, NetManagerServer.this);
                        register(ch, SelectionKey.OP_READ, client.tcpListener);
                        connections.add(client);
                        newConnections.add(client);
                    } else {
                        ch.close();
                    }
                }
            } catch (ClosedByInterruptException e) {
            } catch (AsynchronousCloseException e) {
            } catch (IOException e) {
                if (tcpChannel.isOpen()) {
                    logger.error("Failed to accept connection", e);
                }
            }
        }
    };
}
