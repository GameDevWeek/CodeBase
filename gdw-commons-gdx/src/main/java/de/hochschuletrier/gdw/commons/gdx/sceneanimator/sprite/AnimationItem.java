package de.hochschuletrier.gdw.commons.gdx.sceneanimator.sprite;

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
public class AnimationItem extends Item {

    protected final float angleAdd;
    protected final float scale;
    protected AnimationExtended animation;
    private final boolean flipX;
    private final boolean flipY;
    private final AnimationExtended originalAnimation;

    public AnimationItem(String group, float scale, float startTime, float angle, boolean oriented, boolean flipX, boolean flipY, float opacity, String animation, Getter getter) {
        super(group, startTime, 0, oriented, opacity, getter);
        this.angleAdd = angle;
        this.scale = scale;
        this.startTime = startTime;
        this.flipX = flipX;
        this.flipY = flipY;
        this.animationTime = 0;
        this.animation = getter.getAnimation(animation);
        this.originalAnimation = this.animation;
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
            float x = position.x - hw;
            float y = position.y - hh;
            float scaleX = flipX ? -scale : scale;
            float scaleY = flipY ? scale : -scale;
            DrawUtil.batch.draw(r, x, y, hw, hh, w, h, scaleX, scaleY, angle + angleAdd);
        }
    }

    @Override
    public boolean startAnimation(Animation animation) {
        if(super.startAnimation(animation))
            return true;
        if(animation.animation == null) {
            this.animation = null;
        } else {
            this.animationTime = 0;
            this.animation = getter.getAnimation(animation.animation);
            totalAnimationTime = this.animation.getDuration();
        }
        return true;
    }
    
    @Override
    protected boolean isAnimationDone() {
        return this.animationTime > this.totalAnimationTime;
    }

    @Override
    public void reset(ArrayList<Animation> animations) {
        super.reset(animations);
        
        animation = originalAnimation;
    }
    
    
}
