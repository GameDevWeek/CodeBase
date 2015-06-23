package de.hochschuletrier.gdw.commons.netcode.core;

import de.hochschuletrier.gdw.commons.utils.chainprocessing.ChainJob;
import de.hochschuletrier.gdw.commons.utils.chainprocessing.ChainWorker;
import java.io.EOFException;
import java.io.IOException;

/**
 * Read datagrams from a TCP connection
 *
 * @author Santo Pfingsten
 */
class NetDatagramReader {

    private final ChainWorker<NetMessage, NetConnection> readWorker = new ChainWorker();
    private final ReadHeaderJob readHeaderJob = new ReadHeaderJob();
    private final ReadMessageJob readMessageJob = new ReadMessageJob();
    private final NetMessage message = new NetMessage();
    private NetDatagram datagram;

    public void readFromConnection(NetConnection connection) throws Exception {
        try {
            if (readWorker.isDone()) {
                readWorker.init(readHeaderJob, message, connection);
            }
            readWorker.runFrame(connection);
        } catch (Exception e) {
            if (datagram != null) {
                connection.datagramPool.free(datagram);
                datagram = null;
            }
            readWorker.reset();
            throw e;
        }
    }

    private class ReadHeaderJob extends ChainJob<NetMessage, NetConnection> {

        @Override
        public void init(NetMessage message, NetConnection connection) {
            message.buffer.clear();
            message.buffer.limit(NetConfig.HEADER_SIZE);
        }

        @Override
        public ChainJob process(NetMessage message, NetConnection connection) throws Exception {
            int size = connection.tcpChannel.read(message.buffer);
            if (size == -1) {
                throw new EOFException("End of stream");
            }
            connection.tcpStatistic.numBytesRead += size;
            if (message.buffer.hasRemaining()) {
                return this;
            }

            message.buffer.flip();
            int messageSize = message.getInt();
            long sequenceId = message.getLong();
            short type = message.getShort();

            datagram = connection.datagramPool.obtain(type);
            datagram.messageSize = messageSize;
            datagram.sequenceId = sequenceId;
            datagram.connection = connection;

            connection.tcpStatistic.numDatagramsReceived++;
            if (datagram.getMessageType() != NetMessageType.NONE) {
                assert (messageSize >= 0 && messageSize <= NetConfig.MAX_MESSAGE_SIZE);
                return readMessageJob;
            }

            connection.onDatagramReceived(datagram);
            datagram = null;
            return null;
        }
    }

    private class ReadMessageJob extends ChainJob<NetMessage, NetConnection> {

        @Override
        public void init(NetMessage message, NetConnection connection) {
            message.buffer.clear();
            message.buffer.limit(datagram.messageSize);
        }

        @Override
        public ChainJob process(NetMessage message, NetConnection connection) throws Exception {
            int size = connection.tcpChannel.read(message.buffer);
            if (size == -1) {
                throw new IOException("End of stream");
            }
            connection.tcpStatistic.numBytesRead += size;
            if (message.buffer.hasRemaining()) {
                return this;
            }
            message.buffer.flip();
            datagram.readFromMessage(message);
            connection.onDatagramReceived(datagram);
            datagram = null;
            return null;
        }
    }
}
