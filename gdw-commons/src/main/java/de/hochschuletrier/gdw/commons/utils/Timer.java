package de.hochschuletrier.gdw.commons.utils;

/**
 * Time measuring helper
 *
 * @author Santo Pfingsten
 */
public class Timer {

    private long time = -1;

    public long get() {
        return time == -1 ? -1 : (System.currentTimeMillis() - time);
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

    public void stop() {
        time = -1;
    }

    public boolean isRunning() {
        return time > 0;
    }
}
