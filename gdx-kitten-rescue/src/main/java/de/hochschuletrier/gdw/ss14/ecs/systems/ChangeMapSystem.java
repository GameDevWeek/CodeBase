package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.game.*;

/**
 * Created by Daniel Dreher on 04.10.2014.
 */
public class ChangeMapSystem extends ECSystem
{

    public ChangeMapSystem(EntityManager entityManager)
    {
        super(entityManager, 0);
    }

    @Override
    public void update(float delta)
    {
        if(Game.mapManager != null)
        {
            if(Game.mapManager.isChangingFloor)
            {
                entityManager.deleteAllGameplayRelatedEntitiesExcludingCat();
                Game.mapManager.isChangingFloor = false;
                Game.mapManager.setFloor(Game.mapManager.targetFloor);
                System.out.println("Changed floor to "+ Game.mapManager.targetFloor);

                Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);

                for (Integer entity : entities)
                {
                    CatPhysicsComponent catPhysicsComponent = (CatPhysicsComponent) entityManager.getComponent(entity, PhysicsComponent.class);

                    catPhysicsComponent.mask = (short) (catPhysicsComponent.mask << 1);
                }
            }
        }

    }

    @Override
    public void render()
    {

    }
}
