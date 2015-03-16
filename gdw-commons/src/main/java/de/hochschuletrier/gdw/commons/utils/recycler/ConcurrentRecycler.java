package de.hochschuletrier.gdw.commons.utils.recycler;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Threadsafe object recycler, which creates new objects if not enough are available
 *
 * @author Santo Pfingsten
 */
public class ConcurrentRecycler<T extends Recyclable> {

    private final ConcurrentLinkedQueue<T> free = new ConcurrentLinkedQueue();
    private final Class<T> clazz;

    public ConcurrentRecycler(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Get a new object to work with
     *
     * @return a new or recycled object
     */
    public T get() {
        T object = free.poll();
        if (object != null) {
            object.recycle();
            return object;

        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not create instance of class", e);
        }
    }

    /**
     * Mark an object ready to be re-used
     *
     * @param object the message to free
     */
    public void free(T object) {
        free.add(object);
    }

    public void clear() {
        free.clear();
    }
}
