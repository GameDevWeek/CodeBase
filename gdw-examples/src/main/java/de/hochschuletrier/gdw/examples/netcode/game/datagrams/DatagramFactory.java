package de.hochschuletrier.gdw.examples.netcode.game.datagrams;

import de.hochschuletrier.gdw.commons.netcode.core.NetDatagram;
import de.hochschuletrier.gdw.commons.netcode.core.NetDatagramPool;

public final class DatagramFactory {

    public static final NetDatagramPool POOL = new NetDatagramPool(
            PlayerDatagram.class,
            MoveIntentDatagram.class,
            DestroyEntityDatagram.class
    );

    static <T extends NetDatagram> T create(Class<T> clazz) {
        return POOL.obtain(clazz);
    }
}
