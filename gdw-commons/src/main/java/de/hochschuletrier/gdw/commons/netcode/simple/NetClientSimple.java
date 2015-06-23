package de.hochschuletrier.gdw.commons.netcode.simple;

import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetManagerClient;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagramPool;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A helper class to help avoid writing the same handler code for every game.
 *
 * @author Santo Pfingsten
 */
public class NetClientSimple {

    private static final Logger logger = LoggerFactory.getLogger(NetClientSimple.class);

    protected NetManagerClient manager;
    protected NetConnection connection;
    protected boolean connected;
    protected final NetDatagramDistributor distributor = new NetDatagramDistributor();
    protected final NetDatagramPool datagramPool;
    private Listener listener;

    public NetClientSimple(NetDatagramPool datagramPool) {
        this.datagramPool = datagramPool;
    }

    public void setHandler(NetDatagramHandler handler) {
        distributor.setHandler(handler);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean isRunning() {
        return connection != null && connection.isConnected();
    }

    public void update() {
        if (isRunning()) {
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
        } else if (connected) {
            connected = false;
            if(listener != null) {
                listener.onDisconnect();
            }
        }
    }

    public boolean connect(String ip, int port) {
        try {
            manager = new NetManagerClient(ip, port, datagramPool);
            NetThreadSimple thread = new NetThreadSimple(manager, 500);
            thread.start();
            connection = manager.getConnection();
            connected = true;
            return true;
        } catch (IOException e) {
            connection = null;
            logger.error("Error creating a NetConnection", e);
        }
        return false;
    }

    public void disconnect() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
            connected = false;
            if(listener != null) {
                listener.onDisconnect();
            }
        }
    }

    public NetConnection getConnection() {
        return connection;
    }

    public interface Listener {
        public void onDisconnect();
    }
}
