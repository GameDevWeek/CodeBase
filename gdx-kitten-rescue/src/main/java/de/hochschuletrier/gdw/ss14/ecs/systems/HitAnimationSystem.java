package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.HitAnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

public class HitAnimationSystem extends ECSystem {
    
    private static final float HitFlashCycleDuration = 0.2f;
    private static final Color HitFlashColor = new Color(1,0,0,1);

    public HitAnimationSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void update(float delta) {
        
        Array<Integer> arr = entityManager.getAllEntitiesWithComponents(HitAnimationComponent.class, RenderComponent.class);
        
        for (Integer entity : arr) {
            
            HitAnimationComponent hitComp = entityManager.getComponent(entity, HitAnimationComponent.class);
            RenderComponent renderComp = entityManager.getComponent(entity, RenderComponent.class);
            
            int lastCycleNumber = (int)(hitComp.timeLeft / HitFlashCycleDuration);
            hitComp.timeLeft -= delta;
            int currentCycleNumber = (int)(hitComp.timeLeft / HitFlashCycleDuration);
            
            if (hitComp.timeLeft < 0.0f) {
                
                entityManager.removeComponent(entity, hitComp);
                renderComp.tintColor = null;
                return;
            }            
            
            if (lastCycleNumber != currentCycleNumber) {
                if (renderComp.tintColor == null)
                    renderComp.tintColor = HitFlashColor;
                else
                    renderComp.tintColor = null;
            }
        }
    }

    @Override
    public void render() {
        // Nothing to render
    }

}
