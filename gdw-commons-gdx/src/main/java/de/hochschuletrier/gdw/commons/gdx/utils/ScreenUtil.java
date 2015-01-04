package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

/**
 * Helper class to toggle fullscreen mode
 *
 * @author Santo Pfingsten
 */
public class ScreenUtil {
    
    private static int lastWidth = 800;
    private static int lastHeight = 600;

    public static void toggleFullscreen() {

        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setDisplayMode(lastWidth, lastHeight, false);
        } else {
            lastWidth = Gdx.graphics.getWidth();
            lastHeight = Gdx.graphics.getHeight();
            Graphics.DisplayMode mode = Gdx.graphics.getDesktopDisplayMode();
            Gdx.graphics.setDisplayMode(mode.width, mode.height, true);
        }
    }

    public static void setFullscreen(boolean fullscreen) {
        if (fullscreen != Gdx.graphics.isFullscreen()) {
            toggleFullscreen();
        }
    }
}
