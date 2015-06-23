package de.hochschuletrier.gdw.commons.netcode.core;

import de.hochschuletrier.gdw.commons.utils.pool.Pool;
import de.hochschuletrier.gdw.commons.utils.pool.ReflectionPool;
import java.util.HashMap;
import java.util.Map;

/**
 * Pool datagrams for better performance and memory efficiency.
 *
 * @author Santo Pfingsten
 */
public class NetDatagramPool {

    private final HashMap<Short, Pool<NetDatagram>> pools = new HashMap<>();
    private final HashMap<Class, Short> classToId = new HashMap();
    private final Class[] idToClass;

    public NetDatagramPool(Class<? extends NetDatagram> ...classes) {
        short nextId = NetDatagramType.FIRST_CUSTOM.toID();
        int size = nextId + classes.length;
        idToClass = new Class[size];
        for (Class clazz : classes) {
            classToId.put(clazz, nextId);
            idToClass[nextId] = clazz;
            nextId++;
        }
    }

    public synchronized NetDatagram obtain(short type) {
        Pool<NetDatagram> pool = pools.get(type);
        if (pool == null) {
            if (type < 0 || type >= idToClass.length) {
                throw new IllegalArgumentException("Type is out of bounds");
            }
            Class<? extends NetDatagram> clazz;
            if (type < NetDatagramType.FIRST_CUSTOM.ordinal()) {
                clazz = NetDatagram.class;
            } else {
                clazz = idToClass[type];
            }
            pool = new ReflectionPool(clazz);
            pools.put(type, pool);
        }

        NetDatagram datagram = pool.obtain();
        datagram.init(type);
        return datagram;
    }

    public <T extends NetDatagram> T obtain(Class<T> clazz) {
        Short id = classToId.get(clazz);
        if (id == null) {
            throw new IllegalArgumentException("class " + clazz.getName() + " is not registered in NetDatagramMapper");
        }
        return (T) obtain(id);
    }

    public synchronized void free(NetDatagram datagram) {
        Pool<NetDatagram> pool = pools.get(datagram.getType());
        assert (pool != null);
        pool.free(datagram);
    }
}
