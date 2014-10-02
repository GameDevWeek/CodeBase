package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.LimitedLifetimeComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

/**
 * 
 * @author Milan Rüll
 *
 */
public class LimitedLifetimeSystem extends ECSystem {

    public LimitedLifetimeSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void update(float delta) {
        Array<Integer> arr = entityManager.getAllEntitiesWithComponents(LimitedLifetimeComponent.class);
        
        for (Integer ent : arr) {
            
            LimitedLifetimeComponent limitedLifeComp = entityManager.getComponent(ent, LimitedLifetimeComponent.class);
            limitedLifeComp.currentLifetime += delta;
            
            if (limitedLifeComp.graduallyReduceAlpha) {
                
                RenderComponent rendComp = entityManager.getComponent(ent, RenderComponent.class);
                if (rendComp != null) {
                    
                    if (rendComp.tintColor == null)
                        rendComp.tintColor = new Color(1,1,1,1);
                    
                    rendComp.tintColor.a -= delta / limitedLifeComp.maxLifetime;
                    if (rendComp.tintColor.a < 0.0f)
                        rendComp.tintColor.a = 0.0f;
                }
            }
            
            if (limitedLifeComp.maxLifetime - limitedLifeComp.currentLifetime < 0.0f) {
                entityManager.deleteEntity(ent);
                continue;
            }
        }
    }

    @Override
    public void render() {
        // Nothing to do
    }

}
