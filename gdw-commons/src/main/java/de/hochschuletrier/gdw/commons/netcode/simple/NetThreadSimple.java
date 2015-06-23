package de.hochschuletrier.gdw.commons.netcode.simple;

import de.hochschuletrier.gdw.commons.netcode.core.NetManager;
import java.io.IOException;
import java.nio.channels.ClosedSelectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thread class to run manager update and shut down the manager correctly when all is done.
 *
 * @author Santo Pfingsten
 */
class NetThreadSimple extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(NetThreadSimple.class);
    private final NetManager manager;
    private final long selectTimeout;

    NetThreadSimple(NetManager manager, long selectTimeout) throws IOException {
        setDaemon(true);
        this.manager = manager;
        this.selectTimeout = selectTimeout;
    }

    @Override
    public void run() {
        try {
            while (manager.isRunning()) {
                manager.update(selectTimeout);
            }
        } catch (ClosedSelectorException e) {
            logger.warn("Selector has been closed", e);
        } catch (IOException e) {
            logger.warn("IOException on select()", e);
        }
        if (manager.isRunning()) {
            manager.shutdown();
        }
        manager.onShutdown();
    }
}
