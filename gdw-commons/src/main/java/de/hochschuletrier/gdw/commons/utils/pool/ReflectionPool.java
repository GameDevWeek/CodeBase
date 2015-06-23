package de.hochschuletrier.gdw.commons.utils.pool;

import java.util.Stack;


/**
 * Simple object pool, which creates new objects if not enough are available
 *
 * @author Santo Pfingsten
 */
public class ReflectionPool<T extends Poolable> implements Pool<T> {
    private final Class<T> clazz;
    private final Stack<T> free = new Stack();

    public ReflectionPool(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T obtain() {
        if (!free.isEmpty()) {
            return free.pop();
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not create instance of class", e);
        }
    }

    public void free(T object) {
        object.reset();
        free.push(object);
    }

    public void clear() {
        free.clear();
    }
}
