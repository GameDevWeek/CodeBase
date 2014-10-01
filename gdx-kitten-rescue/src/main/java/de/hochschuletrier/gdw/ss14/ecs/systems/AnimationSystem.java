package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.Component;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

/**
 * Updates all RenderComponents of an Entity with a AnimationComponent.
 * Syncs the entity state to the animation state.
 * @author David Neubauer
 */
public class AnimationSystem extends ECSystem {
    
    public AnimationSystem(EntityManager entityManager, int priority) {
        super(entityManager, priority);
    }

    @Override
    public void update(float delta) {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(RenderComponent.class,
                AnimationComponent.class,
                PhysicsComponent.class);

        AnimationComponent animationCompo;
        RenderComponent renderCompo;
        
        for (Integer entity : entities) {
            animationCompo = entityManager.getComponent(entity, AnimationComponent.class);
            renderCompo = entityManager.getComponent(entity, RenderComponent.class);
            
            //update time
            animationCompo.animationTime += delta;
            
            // Change animation if neccessary (and reset it)
            if(animationCompo.actualAnimationState != getEntityState(entity)) {
                animationCompo.actualAnimationState = getEntityState(entity);
                animationCompo.animationTime = 0;
            }
            
            // update animation-frame
            renderCompo.texture = animationCompo.animation[animationCompo.actualAnimationState].getKeyFrame(animationCompo.animationTime);
            
        }
    }
    
    private int getEntityState(int entity) {
        Component c = null;
        
            c = entityManager.getComponent(entity, CatPropertyComponent.class);
            if(c != null) {
                return ((CatPropertyComponent)c).state.ordinal();
            }
            
            throw new RuntimeException("The entity " + entity + " has no state!");
        
    }

    @Override
    public void render() {
        
    }
}
