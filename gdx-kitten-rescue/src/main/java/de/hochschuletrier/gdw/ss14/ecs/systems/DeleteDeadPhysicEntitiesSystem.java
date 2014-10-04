package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

/**
 * Created by Daniel Dreher on 04.10.2014.
 */
public class DeleteDeadPhysicEntitiesSystem extends ECSystem
{
    private PhysixManager physixManager;

    public DeleteDeadPhysicEntitiesSystem(EntityManager entityManager, PhysixManager physixManager)
    {
        super(entityManager);
        this.physixManager = physixManager;
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);

        for (Integer entity : entities)
        {
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);

            if(physicsComponent.flaggedForRemoval)
            {
                physixManager.destroy(physicsComponent.physicsBody);
                entityManager.deleteEntity(entity);
            }

        }

    }

    @Override
    public void render()
    {

    }
}
