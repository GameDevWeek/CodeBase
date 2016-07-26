package de.hochschuletrier.gdw.commons.gdx.sceneanimator.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.Animation;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.Item;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator.Getter;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import java.util.ArrayList;

/**
 *
 * @author Santo Pfingsten
 */
public class TextureItem extends Item {

    protected final float angleAdd;
    protected final float scale;
    protected final TextureRegion region = new TextureRegion();
    private final Texture originalTexture;

    public TextureItem(String group, float scale, float startTime, float angle, boolean oriented, float opacity, String texture, Getter getter) {
        super(group, startTime, 0, oriented, opacity, getter);
        this.angleAdd = angle;
        this.scale = scale;
        this.startTime = startTime;
        this.animationTime = 0;
        originalTexture = getter.getTexture(texture);
        region.setRegion(originalTexture);
    }

    @Override
    public void render() {
        if (region.getTexture() != null) {
            float w = region.getRegionWidth();
            float h = region.getRegionHeight();
            float hw = w* 0.5f;
            float hh = h* 0.5f;
            DrawUtil.batch.draw(region, position.x - hw, position.y - hh, hw, hh, w, h, scale, scale, angle + angleAdd);
        }
    }

    @Override
    public boolean startAnimation(Animation animation) {
        if(super.startAnimation(animation))
            return true;

        if(animation.animation == null) {
            this.region.setTexture(null);
        } else {
            this.animationTime = 0;
            region.setRegion(getter.getTexture(animation.animation));
        }
        return true;
    }
    
    @Override
    protected boolean isAnimationDone() {
        return true;
    }

    @Override
    public void reset(ArrayList<Animation> animations) {
        super.reset(animations);
        
        region.setRegion(originalTexture);
    }
}
