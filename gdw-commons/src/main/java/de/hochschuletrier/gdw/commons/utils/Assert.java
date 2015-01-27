package de.hochschuletrier.gdw.commons.utils;

public class Assert {

    public static void that(boolean value, String message) {
        if (!value) {
            throw new AssertionError(message);
        }
    }
}
