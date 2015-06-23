package de.hochschuletrier.gdw.commons.netcode.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Manager class for a client
 *
 * @author Santo Pfingsten
 */
public class NetManagerClient extends NetManager {

    private final NetConnection connection;

    public NetManagerClient(String ip, int port, NetDatagramPool datagramPool) throws IOException {
        super(datagramPool);

        SocketChannel tcpChannel = SocketChannel.open(new InetSocketAddress(InetAddress.getByName(ip), port));
        tcpChannel.configureBlocking(false);
        tcpChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        int localPort = ((InetSocketAddress) tcpChannel.getLocalAddress()).getPort();
        udpChannel.bind(new InetSocketAddress(localPort));
        connection = new NetConnection(tcpChannel, udpChannel, datagramPool, this);

        register(tcpChannel, SelectionKey.OP_READ, connection.tcpListener);
    }

    @Override
    protected void readDatagram(SocketAddress from, NetMessage message) {
        if (from.equals(connection.destination)) {
            message.readDatagram(connection);
        }
    }

    @Override
    public boolean isRunning() {
        return connection.isConnected();
    }

    @Override
    public void shutdown() {
        connection.disconnect();
    }

    @Override
    public void onShutdown() {
        if (connection.isConnected()) {
            connection.disconnect();
        }
    }

    @Override
    void onDisconnect(NetConnection connection) {
    }

    public NetConnection getConnection() {
        return connection;
    }
}
