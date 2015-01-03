package de.hochschuletrier.gdw.commons.gdx.menu.widgets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 *
 * @author Santo Pfingsten
 */
public class DecoImage extends Image {
    TextureRegion region;

    public DecoImage(Texture texture) {
        super(null, Scaling.stretch, Align.center);
        region = new TextureRegion(texture);
        setDrawable(new TextureRegionDrawable(region));
		setSize(getPrefWidth(), getPrefHeight());
        
        setBounds(0, 0, texture.getWidth(), texture.getHeight());
        setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);
        setTouchable(Touchable.disabled);
    }
    
    public void setTexture(Texture texture) {
        region.setRegion(texture);
    }
}
