package de.hochschuletrier.gdw.commons.utils.chainprocessing;

public abstract class ChainJob<T, C> {
    public void init(T object, C context) {
        
    }
    public abstract ChainJob<T, C> process(T object, C context) throws Exception;
}
