package de.hochschuletrier.gdw.ss14.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;
import java.util.ArrayList;
import java.util.Comparator;

public class AnimationRenderSystem extends EntitySystem implements EntityListener {

    private static final ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);
    private static final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);
    private static final EntityComparator comparator = new EntityComparator();
    private final ArrayList<Entity> entities = new ArrayList();
    private boolean resort;

    public AnimationRenderSystem() {
        super(0);
    }

    public AnimationRenderSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.getFor(PositionComponent.class, AnimationComponent.class);
        engine.addEntityListener(family, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        entities.add(entity);
        resort = true;
    }

    @Override
    public void entityRemoved(Entity entity) {
        entities.remove(entity);
    }

    @Override
    public void update(float deltaTime) {
        if (resort) {
            entities.sort(comparator);
            resort = false;
        }

        for (Entity entity : entities) {
            AnimationComponent animation = animationMapper.get(entity);
            PositionComponent position = positionMapper.get(entity);

            animation.stateTime += deltaTime;
            TextureRegion keyFrame = animation.animation.getKeyFrame(animation.stateTime);
            int w = keyFrame.getRegionWidth();
            int h = keyFrame.getRegionHeight();
            DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h, 1, 1, position.rotation);
        }
    }

    private static class EntityComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity a, Entity b) {
            AnimationComponent ac = animationMapper.get(a);
            AnimationComponent bc = animationMapper.get(b);
            return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
        }
    }
}
