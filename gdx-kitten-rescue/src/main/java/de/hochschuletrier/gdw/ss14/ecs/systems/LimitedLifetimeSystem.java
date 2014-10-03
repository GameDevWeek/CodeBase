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
            
            if (limitedLifeComp.graduallyReduceAlpha) {
                
                RenderComponent rendComp = entityManager.getComponent(ent, RenderComponent.class);
                if (rendComp != null) {
                    
                    if (rendComp.tintColor == null)
                        rendComp.tintColor = new Color(1,1,1,1);
                    
                    // Remove as much percent of the alpha as percent of the lifetime
                    rendComp.tintColor.a = rendComp.tintColor.a * (1.0f - (delta / (limitedLifeComp.lifetimeLeft)));
                    if (rendComp.tintColor.a < 0.0f)
                        rendComp.tintColor.a = 0.0f;
                }
            }
            
            limitedLifeComp.lifetimeLeft -= delta;
            if (limitedLifeComp.lifetimeLeft < 0.0f) {
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
