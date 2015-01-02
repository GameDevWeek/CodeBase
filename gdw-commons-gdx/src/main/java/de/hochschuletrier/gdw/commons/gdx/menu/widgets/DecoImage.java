package de.hochschuletrier.gdw.commons.gdx.menu.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author Santo Pfingsten
 */
public class DecoImage extends Image {

    public DecoImage(Texture texture) {
        super(texture);
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);
        setTouchable(Touchable.disabled);
    }
}
