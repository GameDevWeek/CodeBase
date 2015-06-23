package de.hochschuletrier.gdw.commons.netcode.core;

import de.hochschuletrier.gdw.commons.utils.pool.Poolable;
import de.hochschuletrier.gdw.commons.utils.pool.Pool;
import de.hochschuletrier.gdw.commons.utils.pool.ReflectionPool;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract manager class
 *
 * @author Santo Pfingsten
 */
public abstract class NetManager {

    private static final Logger logger = LoggerFactory.getLogger(NetManager.class);
    protected final LinkedList<InterestOpsChange> interestOpsChanges = new LinkedList();
    protected Pool<InterestOpsChange> interestPool = new ReflectionPool(InterestOpsChange.class);
    private final NetMessage udpMessage = new NetMessage();
    protected final DatagramChannel udpChannel;
    private final LinkedList<DatagramTask> tasks = new LinkedList();
    private final NetDatagramPool datagramPool;
    private final ReflectionPool<DatagramTask> taskPool = new ReflectionPool(DatagramTask.class);

    protected final Selector selector;

    NetManager(NetDatagramPool datagramPool) throws IOException {
        this.datagramPool = datagramPool;
        selector = Selector.open();
        udpChannel = DatagramChannel.open();
        udpChannel.configureBlocking(false);

        register(udpChannel, SelectionKey.OP_READ, udpListener);
    }

    public void update() throws IOException {
        applyInterestOpsChanges();
        update(selector.select());
    }

    public void update(long timeout) throws IOException {
        applyInterestOpsChanges();
        update(selector.select(timeout));
    }

    public void updateNow() throws IOException {
        applyInterestOpsChanges();
        update(selector.selectNow());
    }

    private void update(int readyChannels) {

        if (readyChannels > 0 && isRunning()) {
            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {

                SelectionKey key = keyIterator.next();

                Listener listener = (Listener) key.attachment();
                final SelectableChannel channel = key.channel();

                if (key.isAcceptable()) {
                    listener.onChannelAcceptable(channel);
                }
                if (key.isReadable()) {
                    listener.onChannelReadable(channel);
                }
                if (key.isWritable()) {
                    listener.onChannelWritable(channel);
                }

                keyIterator.remove();
            }
        }
    }

    public final SelectionKey register(SelectableChannel channel, int flags, Listener listener) throws ClosedChannelException {
        return channel.register(selector, flags, listener);
    }

    public void onDatagramQueueUpdate(SelectableChannel channel, boolean isEmpty) {
        synchronized (interestOpsChanges) {
            InterestOpsChange change = interestPool.obtain();
            change.key = channel.keyFor(selector);
            change.flags = SelectionKey.OP_READ;
            if (!isEmpty) {
                change.flags |= SelectionKey.OP_WRITE;
            }
            interestOpsChanges.add(change);
        }
        selector.wakeup();
    }

    private void applyInterestOpsChanges() {
        synchronized (interestOpsChanges) {
            for (InterestOpsChange change : interestOpsChanges) {
                change.key.interestOps(change.flags);
                interestPool.free(change);
            }
            interestOpsChanges.clear();
        }
    }

    void sendUnreliable(NetDatagram datagram, NetConnection connection) {
        DatagramTask task;
        synchronized(taskPool) {
            task = taskPool.obtain();
        }
        task.datagram = datagram;
        task.connection = connection;

        synchronized (tasks) {
            tasks.add(task);
            onDatagramQueueUpdate(udpChannel, false);
        }
    }

    public static class DatagramTask implements Poolable {

        NetDatagram datagram;
        NetConnection connection;

        @Override
        public void reset() {
            datagram = null;
            connection = null;
        }

    }

    protected abstract void readDatagram(SocketAddress from, NetMessage message);

    private final NetManager.Listener udpListener = new NetManager.Listener() {
        @Override
        public void onChannelWritable(SelectableChannel channel) {
            DatagramTask task;
            synchronized (tasks) {
                task = tasks.poll();
                if (task == null) {
                    onDatagramQueueUpdate(udpChannel, true);
                    return;
                }
            }

            udpMessage.buffer.clear();
            udpMessage.writeToBuffer(task.datagram);

            try {
                udpChannel.send(udpMessage.buffer, task.connection.destination);

                task.connection.udpStatistic.numBytesWritten += udpMessage.buffer.limit();
                task.connection.udpStatistic.numDatagramsSent++;
            } catch (IOException e) {
                logger.error("Error sending datagram", e);
            } finally {
                if (task.datagram.onSendComplete()) {
                    datagramPool.free(task.datagram);
                }
                synchronized(taskPool) {
                    taskPool.free(task);
                }
            }
        }

        @Override
        public void onChannelReadable(SelectableChannel channel) {
            try {
                udpMessage.buffer.clear();
                SocketAddress from = udpChannel.receive(udpMessage.buffer);
                udpMessage.buffer.flip();
                if (from != null) {
                    readDatagram(from, udpMessage);
                }
            } catch (IOException e) {
                logger.error("error receiving datagram", e);
            }
        }
    };

    public abstract boolean isRunning();

    public abstract void shutdown();

    public abstract void onShutdown();

    abstract void onDisconnect(NetConnection connection);

    public static class InterestOpsChange implements Poolable {

        SelectionKey key;
        volatile int flags;

        @Override
        public void reset() {
            key = null;
            flags = 0;
        }
    }

    protected static class Listener {

        public void onChannelWritable(SelectableChannel channel) {
        }

        public void onChannelReadable(SelectableChannel channel) {
        }

        public void onChannelAcceptable(SelectableChannel channel) {
        }
    }
}
