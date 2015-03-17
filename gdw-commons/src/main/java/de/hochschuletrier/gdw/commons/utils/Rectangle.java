package de.hochschuletrier.gdw.commons.utils;

/**
 *
 * @author Santo Pfingsten
 */
public class Rectangle extends Point {

    public int width, height;

    public Rectangle() {
    }

    public Rectangle(int px, int py, int w, int h) {
        super(px, py);
        width = w;
        height = h;
    }
}
