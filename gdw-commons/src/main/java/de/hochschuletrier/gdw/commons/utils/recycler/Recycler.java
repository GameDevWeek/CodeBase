package de.hochschuletrier.gdw.commons.utils.recycler;

import java.util.Stack;

/**
 * Simple object recycler, which creates new objects if not enough are available
 *
 * @author Santo Pfingsten
 */
public class Recycler<T extends Recyclable> {

    private final Stack<T> free = new Stack();
    private final Class<T> clazz;

    public Recycler(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get() {
        if (!free.isEmpty()) {
            T object = free.pop();
            object.recycle();
            return object;
        }

        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not create instance of class", e);
        }
    }

    public void free(T object) {
        free.push(object);
    }

    public void clear() {
        free.clear();
    }
}
