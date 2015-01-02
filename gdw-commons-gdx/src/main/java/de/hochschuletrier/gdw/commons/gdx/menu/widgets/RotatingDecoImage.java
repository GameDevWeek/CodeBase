package de.hochschuletrier.gdw.commons.gdx.menu.widgets;

import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Santo Pfingsten
 */
public class RotatingDecoImage extends DecoImage {

    private final float rotate;

    public RotatingDecoImage(Texture texture, float rotate) {
        super(texture);
        this.rotate = rotate;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setRotation(getRotation() + delta * rotate);
    }
}
