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
import de.hochschuletrier.gdw.ws1415.game.components.BlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Renders all renderable components by using the Render-Subsystems.
 * 
 * @author compie
 *
 */
public class RenderSystem extends EntitySystem implements EntityListener {

    private static final EntityComparator comparator = new EntityComparator();
    private final ArrayList<Entity> entities = new ArrayList<>();
    private boolean resort;

    private AnimationRenderSubsystem animationRenderSystem = new AnimationRenderSubsystem();
    private DestructableBlockRenderSubsystem destructableBlockRenderSystem = new DestructableBlockRenderSubsystem();
    
    public RenderSystem() {
        super(0);
    }

    public RenderSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        @SuppressWarnings("unchecked")
		Family family = Family.all(PositionComponent.class, LayerComponent.class).one(AnimationComponent.class).get();
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

        // Go through all entities and use subsystems to render them depending on their
        // components. Possible alternative: create RenderComponent with a RenderType enum for a
        // simple switch.
        for (Entity entity : entities) {
            AnimationComponent animation = ComponentMappers.animation.get(entity);
            BlockComponent block = ComponentMappers.block.get(entity);
            HealthComponent health = ComponentMappers.health.get(entity);
            
            if(animation != null) {
            	if(block != null && health != null) {
            		destructableBlockRenderSystem.render(entity, deltaTime);
            	} else {
            		animationRenderSystem.render(entity, deltaTime);
            	}
            }
        }
    }

    private static class EntityComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity a, Entity b) {
            LayerComponent ac = ComponentMappers.layer.get(a);
            LayerComponent bc = ComponentMappers.layer.get(b);
            return ac.layer > bc.layer ? 1 : (ac.layer == bc.layer) ? 0 : -1;
        }
    }
}

