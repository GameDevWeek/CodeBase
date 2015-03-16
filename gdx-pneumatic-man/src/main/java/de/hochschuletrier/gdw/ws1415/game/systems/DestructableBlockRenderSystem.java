package de.hochschuletrier.gdw.ws1415.game.systems;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.BlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

public class DestructableBlockRenderSystem extends AnimationRenderSystem
{
    
    private static final EntityComparator comparator = new EntityComparator();
    private final ArrayList<Entity> entities = new ArrayList();
    private boolean resort;
    
    @Override
    public void addedToEngine(Engine engine) {
        Family family = Family.all(PositionComponent.class, AnimationComponent.class, HealthComponent.class, BlockComponent.class).get();
        engine.addEntityListener(family, this);
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
            HealthComponent health = ComponentMappers.health.get(entity);
            
            TextureRegion keyFrame = animation.animation.getKeyFrame(health.Value);
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
