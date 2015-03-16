package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import java.util.ArrayList;
import java.util.Comparator;

public class AnimationRenderSystem extends EntitySystem implements EntityListener {

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
        Family family = Family.all(PositionComponent.class, AnimationComponent.class).get();
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
            AnimationComponent animation = ComponentMappers.animation.get(entity);
            PositionComponent position = ComponentMappers.position.get(entity);

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
            AnimationComponent ac = ComponentMappers.animation.get(a);
            AnimationComponent bc = ComponentMappers.animation.get(b);
            return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
        }
    }
}
