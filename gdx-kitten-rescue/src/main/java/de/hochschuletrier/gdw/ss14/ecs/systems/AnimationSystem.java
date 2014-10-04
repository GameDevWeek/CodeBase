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
                AnimationComponent.class);

        AnimationComponent animationCompo;
        RenderComponent renderCompo;
        PhysicsComponent physicsCompo;
        
        for (Integer entity : entities) {
            animationCompo = entityManager.getComponent(entity, AnimationComponent.class);
            renderCompo = entityManager.getComponent(entity, RenderComponent.class);
            physicsCompo = entityManager.getComponent(entity, PhysicsComponent.class);
            
            AnimationExtended currentAnim = animationCompo.animation.get(animationCompo.currentAnimationState);
            int state = getEntityState(entity);
            if (state < 0) continue;
            if (currentAnim == null) {
                renderCompo.texture = null;
                continue;
            }
            
            //set isFinished flag of animationCompo
            animationCompo.isFinished = currentAnim.animationDuration <= animationCompo.animationTime;
            if(currentAnim.getPlayMode() != AnimationExtended.PlayMode.NORMAL)
                animationCompo.isFinished = false;
                    
            
            //update time
            if (physicsCompo != null && animationCompo.speedUpFactor.get(animationCompo.currentAnimationState) != null) {
                animationCompo.animationTime
                        += delta * physicsCompo.getVelocity().len()
                        / animationCompo.speedUpFactor.get(animationCompo.currentAnimationState);
            } else {
                animationCompo.animationTime += delta;
            }
            
            // Change animation if neccessary (and reset it)
            if(animationCompo.currentAnimationState != state) {
                animationCompo.currentAnimationState = state;
                animationCompo.animationTime = 0;
            }
            
            // update animation-frame
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
