package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.LimitedLifetimeComponent;

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
