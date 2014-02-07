package de.hochschuletrier.gdw.commons.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Utilities for quietly doing things (ignoring exceptions)
 *
 * @author Santo Pfingsten
 */
public class QuietUtils {

    public static void sleep(int ms) {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
        }
    }
    
    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
