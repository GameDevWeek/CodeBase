package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;

/**
 *
 * @author Santo Pfingsten
 */
public class DrawUtil {

    private static Color currentColor = Color.WHITE.cpy();
    private static int screenWidth;
    private static int screenHeight;
    private static Mode currentMode = Mode.NORMAL;
    public static SpriteBatch batch = new SpriteBatch();
    public static ImageX white;

    public enum Mode {

        NORMAL,
        ALPHA_MAP,
        ALPHA_BLEND,
        COLOR_MULTIPLY,
        ADD,
        SCREEN
    }

    public static void init(int width, int height) {
        screenWidth = width;
        screenHeight = height;

        // create a white image for filling rects
        white = new ImageX(Color.WHITE);
    }

    public static void updateCamera(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        screenWidth = (int) camera.viewportWidth;
        screenHeight = (int) camera.viewportHeight;
    }

    public static void scale(float sx, float sy) {
        Gdx.gl11.glScalef(sx, sy, 1);
    }

    public static void rotate(float x, float y, float angle) {
        Gdx.gl11.glTranslatef(x, y, 0);
        Gdx.gl11.glRotatef(angle, 0, 0, 1);
        Gdx.gl11.glTranslatef(-x, -y, 0);
    }

    public static void translate(float x, float y) {
        Gdx.gl11.glTranslatef(x, y, 0);
    }

    public static void setClip(int x, int y, int width, int height) {
        batch.flush();
        Gdx.gl11.glEnable(GL11.GL_SCISSOR_TEST);
        Gdx.gl11.glScissor(x, screenHeight - y - height, width, height);
    }

    public static void clearClip() {
        batch.flush();
        Gdx.gl11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void pushTransform() {
        batch.flush();
        Gdx.gl11.glPushMatrix();
    }

    public static void popTransform() {
        batch.flush();
        Gdx.gl11.glPopMatrix();
    }

    public static void copyArea(Texture target, int x, int y, int format) {
        target.bind();
        Gdx.gl11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, format,
                x, screenHeight - (y + target.getHeight()),
                target.getWidth(), target.getHeight(), 0);
    }

    public static void setDrawMode(Mode mode) {

        currentMode = mode;
        switch (currentMode) {
            case NORMAL:
                batch.enableBlending();
                batch.flush();
                Gdx.gl11.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                break;
            case ALPHA_MAP:
                batch.disableBlending();
                batch.flush();
                Gdx.gl11.glColorMask(false, false, false, true);
                break;
            case ALPHA_BLEND:
                batch.enableBlending();
                batch.flush();
                Gdx.gl11.glColorMask(true, true, true, false);
                batch.setBlendFunction(GL11.GL_DST_ALPHA, GL11.GL_ONE_MINUS_DST_ALPHA);
                break;
            case COLOR_MULTIPLY:
                batch.enableBlending();
                batch.flush();
                Gdx.gl11.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL11.GL_ONE_MINUS_SRC_COLOR, GL11.GL_SRC_COLOR);
                break;
            case ADD:
                batch.enableBlending();
                batch.flush();
                Gdx.gl11.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL11.GL_ONE, GL11.GL_ONE);
                break;
            case SCREEN:
                batch.enableBlending();
                batch.flush();
                Gdx.gl11.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
                break;
            default:
                throw new AssertionError(currentMode.name());
        }
    }

    public static void clearAlphaMap() {
        pushTransform();
        Gdx.gl11.glLoadIdentity();

        Mode lastMode = currentMode;
        setDrawMode(Mode.ALPHA_MAP);
        setColor(Color.CLEAR);
        fillRect(0, 0, screenWidth, screenHeight);
        resetColor();
        setDrawMode(lastMode);

        popTransform();
    }

    public static void clearColor(Color color) {
        Gdx.gl11.glClearColor(color.r, color.g, color.b, color.a);
    }

    public static void clear() {
        Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public static void resetColor() {
        setColor(Color.WHITE);
    }

    public static void setColor(Color color) {
        currentColor.set(color);
        batch.setColor(currentColor);
        Gdx.gl11.glColor4f(color.r, color.g, color.b, color.a);
    }

    public static Color getColor() {
        return currentColor;
    }

    public static void fillRect(float x, float y, float width, float height) {
        white.draw(x, y, width, height);
    }

    public static void drawRect(float x, float y, float width, float height) {
        fillRect(x, y, width, 1);
        fillRect(x, y + height - 1, width, 1);
        fillRect(x, y, 1, height);
        fillRect(x + width - 1, y, 1, height);
    }

    public static void setLineWidth(float width) {
        Gdx.gl11.glPointSize(width);
        Gdx.gl11.glLineWidth(width);
    }
}
