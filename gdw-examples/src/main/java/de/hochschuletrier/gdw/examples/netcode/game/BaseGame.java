package de.hochschuletrier.gdw.examples.netcode.game;

import de.hochschuletrier.gdw.commons.utils.QuietUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Ignore what's in here, it's just a quick hack to get the app running and drawing.
 * 
 * @author Santo Pfingsten
 */
public abstract class BaseGame extends JFrame implements Runnable, KeyListener {

    private boolean running = true;
    private final static ImageIcon dot = new ImageIcon("src/main/resources/dot.png");
    private final static ImageIcon dotChanged = new ImageIcon("src/main/resources/dot_changed.png");

    public class Entity {

        private final long id;
        private int moveX, moveY;
        private final JLabel label;
        private long lastMessage;

        public Entity(long id) {
            this.id = id;
            label = new JLabel(dot);
            add(label);
            label.setVisible(true);
        }

        public final void setPosition(int x, int y) {
            label.setBounds(x - 8, y - 8, 16, 16);
        }

        public final void move(int x, int y) {
        }

        public final void move() {
            if (moveX != 0 || moveY != 0) {
                setPosition(getX() + moveX, getY() + moveY);
            }
        }

        public long getID() {
            return id;
        }

        public int getX() {
            return label.getX() + 8;
        }

        public int getY() {
            return label.getY() + 8;
        }

        public void setChanged(boolean changed) {
            label.setIcon(changed ? dotChanged : dot);
        }

        public void setMoveDirection(int x, int y) {
            moveX = x;
            moveY = y;
        }

        public long getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage() {
            lastMessage = System.currentTimeMillis();
        }

        public void destroy() {
            BaseGame.this.remove(label);
            BaseGame.this.repaint();
        }
    }

    public BaseGame(String title) {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(this);
        setSize(new Dimension(640, 480));
        setVisible(true);
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
    }

    public abstract void update();

    @Override
    public void run() {
        while (running) {
            update();
            QuietUtils.sleep(10);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
