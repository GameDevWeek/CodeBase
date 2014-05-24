package de.hochschuletrier.gdw.commons.utils;

import java.util.Stack;

public class Recycler<E> {

    private final Stack<E> free = new Stack();
    private final Class<? extends E> clazz;

    public Recycler(Class<? extends E> clazz) {
        this.clazz = clazz;
    }

    public E get() {
        if (!free.isEmpty()) {
            return free.pop();
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create instance of class", e);
        }
    }

    public void recycle(E object) {
        free.push(object);
    }

    public void clear() {
        free.clear();
    }
}
