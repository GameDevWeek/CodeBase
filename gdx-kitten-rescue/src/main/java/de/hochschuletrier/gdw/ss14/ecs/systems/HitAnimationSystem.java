package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.HitAnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

public class HitAnimationSystem extends ECSystem {
    
    private static final float hitFlashCycleDuration = 0.1f;

    public HitAnimationSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void update(float delta) {
        
        Array<Integer> arr = entityManager.getAllEntitiesWithComponents(HitAnimationComponent.class, RenderComponent.class);
        
        for (Integer entity : arr) {
            
            HitAnimationComponent hitComp = entityManager.getComponent(entity, HitAnimationComponent.class);
            RenderComponent renderComp = entityManager.getComponent(entity, RenderComponent.class);
            
            int lastCycleNumber = (int)(hitComp.timeLeft / hitFlashCycleDuration);
            hitComp.timeLeft -= delta;
            int currentCycleNumber = (int)(hitComp.timeLeft / hitFlashCycleDuration);
            
            if (hitComp.timeLeft < 0.0f) {
                
                // TODO: Remove component from entity
                //entityManager.
                // renderComp.isTintedRed = false;
            }            
            
            /*else*/ if (lastCycleNumber != currentCycleNumber)
                renderComp.isTintedRed = !renderComp.isTintedRed;
            
            System.out.println(renderComp.isTintedRed);
        }
    }

    @Override
    public void render() {
        // Nothing to render
    }

}
