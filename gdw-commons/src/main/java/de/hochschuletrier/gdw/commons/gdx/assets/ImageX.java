package de.hochschuletrier.gdw.commons.gdx.assets;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Santo Pfingsten
 */
public class ImageX {

    protected final Texture texture;

    public ImageX(Texture texture) {
        this.texture = texture;
    }

    public ImageX(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void draw() {
        assert(texture != null);
        draw(0, 0, texture.getWidth(), texture.getHeight());
    }

    public void draw(float x, float y) {
        assert(texture != null);
        draw(x, y, texture.getWidth(), texture.getHeight());
    }

    public void draw(float x, float y, float width, float height) {
        assert(texture != null);
        DrawUtil.batch.draw(texture, x, y, width, height, 0, 0, texture.getWidth(), texture.getHeight(), false, true);
    }

    public void draw(float x, float y, float scale) {
        assert(texture != null);
        draw(x, y, texture.getWidth() * scale, texture.getHeight() * scale);
    }

    public void draw(float x, float y, int width, int height, int srcX, int srcY) {
        assert(texture != null);
        DrawUtil.batch.draw(texture, x, y, width, height, srcX, srcY, width, height, false, true);
    }

    public void draw(float x, float y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
        assert(texture != null);
        DrawUtil.batch.draw(texture, x, y, width, height, srcX, srcY, srcWidth, srcHeight, false, true);
    }

    public int getWidth() {
        assert(texture != null);
        return texture.getWidth();
    }

    public int getHeight() {
        assert(texture != null);
        return texture.getHeight();
    }

    public void dispose() {
        assert(texture != null);
        texture.dispose();
    }

    public void bind() {
        assert(texture != null);
        texture.bind();
    }
}
