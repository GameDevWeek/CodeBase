package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

/**
 * 
 * Subsystem used by the RenderSystem.
 *
 */
public class AnimationRenderSubsystem {

    void render(Entity entity, float deltaTime) {
        AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);

        animation.stateTime += deltaTime;
        TextureRegion keyFrame = animation.animation
                .getKeyFrame(animation.stateTime);
        if (animation.stateTime > animation.animation.animationDuration) {
            animation.animationFinished = true;
            animation.stateTime %= animation.animation.animationDuration;
        }

        int w = keyFrame.getRegionWidth();
        int h = keyFrame.getRegionHeight();
        DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h
                * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
    }
}
