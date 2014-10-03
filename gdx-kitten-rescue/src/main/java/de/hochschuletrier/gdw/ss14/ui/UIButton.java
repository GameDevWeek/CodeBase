package de.hochschuletrier.gdw.ss14.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;

public class UIButton extends Button {
    Array<Drawable> overAnimation;
    float frameDuration;
    float stateDuration;
    int currentFrame;

    public UIButton(Skin skin) {
        super(skin);
        setSkin(skin);
    }

    public UIButton(Skin skin, String styleName) {
        super(skin, styleName);
        setSkin(skin);
    }

    public UIButton(Actor child, Skin skin, String styleName) {
        super(child, skin, styleName);
        setSkin(skin);
    }

    public UIButton(Actor child, ButtonStyle style) {
        super(child, style);
    }

    public UIButton(ButtonStyle style) {
        super(style);
    }

    /**
     * Creates a button without setting the style or size. At least a style must
     * be set before using this button.
     */
    public UIButton() {
        super();
    }

    public void setOverAnimation(Skin skin, String filename, float frameDuration) {
        Array<AtlasRegion> regions = skin.getAtlas().findRegions(filename);
        for (AtlasRegion atlasRegion : regions) {
            Drawable drawable = new SpriteDrawable(skin.getSprite(filename));
            overAnimation.add(drawable);
        }
        this.frameDuration = frameDuration;
        currentFrame = 0;
    }

    @Override
    public void act(float delta) {
        // TODO Auto-generated method stub
        super.act(delta);
        if(stateDuration >= frameDuration){
            currentFrame++;
        }
        
        stateDuration += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();
        ButtonStyle style = getStyle();
        Drawable background = null;
        float offsetX = 0, offsetY = 0;
        if (isPressed() && !isDisabled()) {
            background = style.down == null ? style.up : style.down;
            offsetX = style.pressedOffsetX;
            offsetY = style.pressedOffsetY;
        } else {
            if (isDisabled() && style.disabled != null)
                background = style.disabled;
            else if (isChecked() && style.checked != null)
                background = (isOver() && style.checkedOver != null) ? style.checkedOver
                        : style.checked;
            else if (isOver() )
                background = overAnimation.get(currentFrame);
            else
                background = style.up;
            offsetX = style.unpressedOffsetX;
            offsetY = style.unpressedOffsetY;
        }
        setBackground(background, false);

        Array<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++)
            children.get(i).moveBy(offsetX, offsetY);
        super.draw(batch, parentAlpha);
        for (int i = 0; i < children.size; i++)
            children.get(i).moveBy(-offsetX, -offsetY);
    }

}
