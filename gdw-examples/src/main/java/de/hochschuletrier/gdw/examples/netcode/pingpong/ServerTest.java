package de.hochschuletrier.gdw.examples.netcode.pingpong;

import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.commons.utils.QuietUtils;
import de.hochschuletrier.gdw.examples.netcode.pingpong.datagrams.ChatDatagram;
import de.hochschuletrier.gdw.examples.netcode.pingpong.datagrams.DatagramFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class ServerTest implements NetDatagramHandler, NetServerSimple.Listener {

    private final NetServerSimple netServer = new NetServerSimple(DatagramFactory.POOL);

    private void run() {
        if (netServer.start(9090, 10)) {
            System.out.println("server running on port 9090");
            while (netServer.isRunning()) {
                netServer.update();
                QuietUtils.sleep(16);
            }
            System.out.println("server shut down");
        }
    }

    public static ServerTest create() {
        final ServerTest instance = new ServerTest();
        instance.netServer.setHandler(instance);
        instance.netServer.setListener(instance);
        return instance;
    }

    public void handle(ChatDatagram datagram) {
        long time = System.currentTimeMillis() - datagram.getTimestamp();
        System.out.printf("%s (%d ms)\n", datagram.getText(), time);

        datagram.getConnection().sendUnreliable(ChatDatagram.create("Pong"));
    }

    @Override
    public boolean onConnect(NetConnection connection) {
        System.out.println("Client connected");
        return true;
    }

    @Override
    public void onDisconnect(NetConnection connection) {
        switch (connection.getStatus()) {
            case INITIAL:
            case CONNECTING:
                System.out.println("Client failed connecting");
                break;
            case TIMED_OUT:
                System.out.println("Client timed out");
                break;
            default:
                System.out.println("Client disconnected");
        }
    }

    public static void main(String[] args) {
        ServerTest test = ServerTest.create();
        test.run();
    }
}
