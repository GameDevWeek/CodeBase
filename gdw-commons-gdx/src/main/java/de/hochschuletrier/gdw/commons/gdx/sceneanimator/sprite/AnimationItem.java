package de.hochschuletrier.gdw.commons.gdx.sceneanimator.sprite;

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
public class AnimationItem extends Item {

    protected float angleAdd;
    protected float scale;
    protected AnimationExtended animation;
    private final Getter getter;

    public AnimationItem(String group, float scale, float startTime, float angle, boolean oriented, float opacity, String animation, Getter getter) {
        super(group, startTime, 0, oriented, opacity);
        this.angleAdd = angle;
        this.scale = scale;
        this.startTime = startTime;
        this.getter = getter;
        this.animationTime = 0;
        this.animation = getter.getAnimation(animation);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        animationTime += deltaTime;
    }

    @Override
    public void render() {
        if (animation != null) {
            TextureRegion r = animation.getKeyFrame(animationTime);
            float w = r.getRegionWidth();
            float h = r.getRegionHeight();
            float hw = w* 0.5f;
            float hh = h* 0.5f;
            DrawUtil.batch.draw(r, position.x - hw, position.y - hh, hw, hh, w, h, scale, scale, angle + angleAdd);
        }
    }

    @Override
    public void startAnimation(Animation animation) {
        if(animation.animation == null) {
            this.animation = null;
        } else {
            this.animationTime = 0;
            this.animation = getter.getAnimation(animation.animation);
        }
    }
}
