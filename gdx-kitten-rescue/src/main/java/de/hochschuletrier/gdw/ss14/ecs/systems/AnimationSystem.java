package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.Component;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPropertyComponent;
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
            int state = getEntityState(entity);
            if (state < 0) continue;
            if (animationCompo.animation.get(animationCompo.currentAnimationState) == null) continue;
            
            //set isFinished flag of animationCompo
            animationCompo.isFinished = 
                    animationCompo.animation.get(animationCompo.currentAnimationState).animationDuration 
                    <= animationCompo.animationTime;
            if(animationCompo.animation.get(animationCompo.currentAnimationState).getPlayMode() != AnimationExtended.PlayMode.NORMAL)
                animationCompo.isFinished = false;
                    
            
            //update time
            animationCompo.animationTime += delta;
            
            // Change animation if neccessary (and reset it)
            if(animationCompo.currentAnimationState != state) {
                animationCompo.currentAnimationState = state;
                animationCompo.animationTime = 0;
            }
            
            // update animation-frame
            AnimationExtended currentAnim = animationCompo.animation.get(animationCompo.currentAnimationState);
            if (currentAnim != null)
                renderCompo.texture = currentAnim.getKeyFrame(animationCompo.animationTime);
            
        }
    }
    
    private int getEntityState(int entity) {
        Component c = null;
        
            c = entityManager.getComponent(entity, CatPropertyComponent.class);
            if(c != null) {
                return ((CatPropertyComponent)c).getState().ordinal();
            }
            c = entityManager.getComponent(entity, DogPropertyComponent.class);
            if(c != null) {
                return ((DogPropertyComponent)c).state.ordinal();
            }
            
            return -1;
        
    }

    @Override
    public void render() {
        
    }
}
