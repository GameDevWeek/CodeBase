package de.hochschuletrier.gdw.commons.utils.pool;

/**
 *
 * @author Santo Pfingsten
 */
public interface Pool<T extends Poolable> {

    void clear();

    void free(T object);

    /**
     * Obtain a new object to work with
     *
     * @return a new or recycled object
     */
    T obtain();
}
