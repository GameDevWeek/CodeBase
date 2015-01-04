package de.hochschuletrier.gdw.commons.gdx.sceneanimator.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.Animation;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.Item;
import de.hochschuletrier.gdw.commons.gdx.sceneanimator.SceneAnimator.Getter;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 *
 * @author Santo Pfingsten
 */
public class TextureItem extends Item {

    protected float angleAdd;
    protected float scale;
    protected final TextureRegion region = new TextureRegion();
    private final Getter getter;

    public TextureItem(String group, float scale, float startTime, float angle, boolean oriented, float opacity, String texture, Getter getter) {
        super(group, startTime, 0, oriented, opacity);
        this.angleAdd = angle;
        this.scale = scale;
        this.startTime = startTime;
        this.getter = getter;
        this.animationTime = 0;
        region.setRegion(getter.getTexture(texture));
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
    public void startAnimation(Animation animation) {
        if(animation.animation == null) {
            this.region.setTexture(null);
        } else {
            this.animationTime = 0;
            region.setRegion(getter.getTexture(animation.animation));
        }
    }
}
