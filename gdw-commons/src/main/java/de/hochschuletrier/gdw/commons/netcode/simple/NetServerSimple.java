package de.hochschuletrier.gdw.commons.netcode.simple;

import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetManagerServer;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagramPool;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class to help avoid writing the same handler code for every game.
 *
 * @author Santo Pfingsten
 */
public class NetServerSimple implements NetDatagramHandler {

    private static final Logger logger = LoggerFactory.getLogger(NetServerSimple.class);

    protected NetManagerServer manager;
    protected final List<NetConnection> connections = new ArrayList<>();
    protected final NetDatagramDistributor distributor = new NetDatagramDistributor();
    protected Listener listener;
    protected final NetDatagramPool datagramPool;

    public NetServerSimple(NetDatagramPool datagramPool) {
        this.datagramPool = datagramPool;
    }

    public void setHandler(NetDatagramHandler handler) {
        distributor.setHandler(handler);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean isRunning() {
        return manager != null && manager.isRunning();
    }

    public List<NetConnection> getConnections() {
        return connections;
    }

    public void update() {
        if (isRunning()) {
            getNewConnections();
            updateConnections();
        }
    }

    protected void updateConnections() {
        Iterator<NetConnection> it = connections.iterator();
        while (it.hasNext()) {
            NetConnection connection = it.next();

            while (connection.hasIncoming()) {
                NetDatagram datagram = connection.receive();
                if (datagram != null) {
                    try {
                        distributor.handle(datagram);
                    } catch (InvocationTargetException e) {
                        logger.error("Error calling handle() for datagram", e);
                    } finally {
                        datagramPool.free(datagram);
                    }
                }
            }

            if (!connection.isConnected()) {
                it.remove();
                if (listener != null) {
                    listener.onDisconnect(connection);
                }
            }
        }
    }

    protected void getNewConnections() {
        if (isRunning() && listener != null) {
            NetConnection connection = manager.pollNewConnection();
            while (connection != null) {
                if (listener.onConnect(connection)) {
                    connections.add(connection);
                } else {
                    connection.disconnect();
                }
                connection = manager.pollNewConnection();
            }
        }
    }

    public boolean start(int port, int maxConnections) {
        try {
            manager = new NetManagerServer(port, maxConnections, datagramPool);
            NetThreadSimple thread = new NetThreadSimple(manager, 500);
            thread.start();
            return true;
        } catch (IOException e) {
            logger.error("Error creating a server", e);
        }
        return false;
    }
    
    public void disconnect() {
        if(isRunning()) {
            manager.shutdown();
        }
    }
    
    public void broadcastUnreliable(NetDatagram datagram, List<NetConnection> connections) {
        if (!connections.isEmpty()) {
            datagram.setBroadcastCount(connections.size());
            for (NetConnection c : connections) {
                c.sendUnreliable(datagram);
            }
        } else {
            datagramPool.free(datagram);
        }
    }

    public void broadcastUnreliable(NetDatagram datagram) {
        broadcastUnreliable(datagram, connections);
    }

    public void broadcastReliable(NetDatagram datagram, List<NetConnection> connections) {
        if (!connections.isEmpty()) {
            datagram.setBroadcastCount(connections.size());
            for (NetConnection c : connections) {
                c.sendReliable(datagram);
            }
        } else {
            datagramPool.free(datagram);
        }
    }

    public void broadcastReliable(NetDatagram datagram) {
        broadcastReliable(datagram, connections);
    }

    public interface Listener {

        public boolean onConnect(NetConnection connection);

        public void onDisconnect(NetConnection connection);
    }

}
