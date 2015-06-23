package de.hochschuletrier.gdw.examples.netcode.pingpong.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagramPool;

/**
 *
 * @author Santo Pfingsten
 */
public final class DatagramFactory {

    public static final NetDatagramPool POOL = new NetDatagramPool(
            ChatDatagram.class
    );

    static <T extends NetDatagram> T create(Class<T> clazz) {
        return POOL.obtain(clazz);
    }
}
