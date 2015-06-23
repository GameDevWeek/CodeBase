package de.hochschuletrier.gdw.commons.netcode.core;

import de.hochschuletrier.gdw.commons.utils.chainprocessing.ChainJob;
import de.hochschuletrier.gdw.commons.utils.chainprocessing.ChainWorker;

/**
 * Write datagrams to a TCP connection
 *
 * @author Santo Pfingsten
 */
class NetDatagramWriter {

    private final ChainWorker<NetDatagram, NetConnection> writeWorker = new ChainWorker();
    private final WriteMessageJob writeMessageJob = new WriteMessageJob();
    private final NetMessage message = new NetMessage();

    public void writeToConnection(NetConnection connection) throws Exception {
        try {
            if (writeWorker.isDone()) {
                NetDatagram datagram = connection.pollOutDatagramReliable();
                if (datagram == null) {
                    return; // Nothing left to write
                }
                writeWorker.init(writeMessageJob, datagram, connection);
            }
            writeWorker.runFrame(connection);
        } catch (Exception e) {
            NetDatagram datagram = writeWorker.getObject();
            if (datagram != null && datagram.onSendComplete()) {
                connection.datagramPool.free(datagram);
            }
            writeWorker.reset();
            throw e;
        }
    }

    private class WriteMessageJob extends ChainJob<NetDatagram, NetConnection> {

        @Override
        public void init(NetDatagram datagram, NetConnection connection) {
            message.buffer.clear();
            message.writeToBuffer(datagram);
        }

        @Override
        public ChainJob process(NetDatagram datagram, NetConnection connection) throws Exception {
            connection.tcpStatistic.numBytesWritten += connection.tcpChannel.write(message.buffer);
            if (message.buffer.hasRemaining()) {
                return this;
            }

            connection.tcpStatistic.numDatagramsSent++;
            if (datagram.onSendComplete()) {
                connection.datagramPool.free(datagram);
            }
            return null;
        }
    }
}
