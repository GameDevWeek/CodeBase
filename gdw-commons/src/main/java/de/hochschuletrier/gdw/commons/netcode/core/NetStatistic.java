package de.hochschuletrier.gdw.commons.netcode.core;

/**
 * Statistics data for UDP & TCP connections
 *
 * @author Santo Pfingsten
 */
public class NetStatistic {

    volatile long numBytesRead;
    volatile long numBytesWritten;
    volatile long numDatagramsReceived;
    volatile long numDatagramsSent;

    public long getNumBytesRead() {
        return numBytesRead;
    }

    public long getNumBytesWritten() {
        return numBytesWritten;
    }

    public long getNumDatagramsReceived() {
        return numDatagramsReceived;
    }

    public long getNumDatagramsSent() {
        return numDatagramsSent;
    }
}
