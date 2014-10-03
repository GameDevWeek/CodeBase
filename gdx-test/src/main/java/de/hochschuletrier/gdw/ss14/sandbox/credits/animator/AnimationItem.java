package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 *
 * @author Santo Pfingsten
 */
public class AnimationItem extends Item {

    protected float scale;
    protected Texture texture;
    protected AnimationExtended animation;

    @Override
    public void update(float deltaTime) {
        animationTime += deltaTime;
    }

    @Override
    public void render() {
        if (texture != null) {
            float w = texture.getWidth() * scale;
            float h = texture.getHeight() * scale;
            DrawUtil.batch.draw(texture, position.x - w * 0.5f, position.y - h * 0.5f, w, h);
        } else if(animation != null) {
            TextureRegion r = animation.getKeyFrame(animationTime);
            float w = r.getRegionWidth() * scale;
            float h = r.getRegionHeight() * scale;
            DrawUtil.batch.draw(r, position.x - w * 0.5f, position.y - h * 0.5f, w, h);
        }
    }

    @Override
    public void startAnimation(AnimatorData.Path.Animation animation) {

    }
}
