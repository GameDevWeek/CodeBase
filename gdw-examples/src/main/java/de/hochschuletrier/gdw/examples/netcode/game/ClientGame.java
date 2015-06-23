package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.netcode.core.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.simple.NetDatagramHandler;
import de.hochschuletrier.gdw.commons.netcode.simple.NetClientSimple;
import de.hochschuletrier.gdw.examples.netcode.game.datagrams.DatagramFactory;
import de.hochschuletrier.gdw.examples.netcode.game.datagrams.DestroyEntityDatagram;
import de.hochschuletrier.gdw.examples.netcode.game.datagrams.MoveIntentDatagram;
import de.hochschuletrier.gdw.examples.netcode.game.datagrams.PlayerDatagram;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 *
 * @author Santo Pfingsten
 */
public class ClientGame extends BaseGame implements NetDatagramHandler {

    private final NetClientSimple netClient = new NetClientSimple(DatagramFactory.POOL);
    private final HashMap<Long, Entity> entityMap = new HashMap<>();
    private boolean moveChanged;
    private int moveX, moveY;
    private NetConnection connection;

    private ClientGame() {
        super("NetPack Game Client Example");

        if (!netClient.connect("localhost", 9090)) {
            System.exit(-1);
        }
        connection = netClient.getConnection();
    }

    public static ClientGame create() {
        final ClientGame instance = new ClientGame();
        instance.netClient.setHandler(instance);
        return instance;
    }

    @Override
    public void update() {
        netClient.update();

        // Render the dot red when updates have been received, white otherwise (messages have been deltified away by the server)
        for (Entity player : entityMap.values()) {
            player.setChanged((System.currentTimeMillis() - player.getLastMessage()) < 50);
        }
    }

    public void handle(DestroyEntityDatagram datagram) {
        destroyEntity(datagram.getID());
    }

    public void handle(PlayerDatagram datagram) {
        Entity entity = findOrCreateEntity(datagram.getID());
        entity.setPosition(datagram.getX(), datagram.getY());
        entity.setLastMessage();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                moveY = setMove(moveY, -1);
                break;
            case KeyEvent.VK_S:
                moveY = setMove(moveY, 1);
                break;
            case KeyEvent.VK_A:
                moveX = setMove(moveX, -1);
                break;
            case KeyEvent.VK_D:
                moveX = setMove(moveX, 1);
                break;
        }

        if (moveChanged) {
            sendMoveIntent();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
                moveY = setMove(moveY, 0);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                moveX = setMove(moveX, 0);
                break;
        }
        if (moveChanged) {
            sendMoveIntent();
        }
    }

    protected int setMove(int oldValue, int value) {
        if (oldValue != value) {
            moveChanged = true;
        }
        return value;
    }

    private void sendMoveIntent() {
        MoveIntentDatagram intent = MoveIntentDatagram.create(moveX, moveY);
        connection.sendReliable(intent);
        moveChanged = false;
    }

    public Entity findOrCreateEntity(long id) {
        Entity entity = entityMap.get(id);
        if (entity == null) {
            entity = new Entity(id);
            entityMap.put(id, entity);
        }
        return entity;
    }

    public void destroyEntity(long id) {
        entityMap.remove(id).destroy();
    }

    public static void main(String[] argv) {
        ClientGame game = ClientGame.create();
        game.start();
    }
}
