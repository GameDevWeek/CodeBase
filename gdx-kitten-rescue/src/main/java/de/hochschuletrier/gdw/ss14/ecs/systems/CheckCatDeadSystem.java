package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

/**
 * Created by Daniel Dreher on 02.10.2014.
 */
public class CheckCatDeadSystem extends ECSystem
{
    private PhysixManager physixManager;

    public CheckCatDeadSystem(EntityManager entityManager, PhysixManager physixManager)
    {
        super(entityManager, 0);
        this.physixManager = physixManager;
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class, PhysicsComponent.class, MovementComponent.class);

        for (Integer entity : entities)
        {
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
            MovementComponent movementComponent = entityManager.getComponent(entity, MovementComponent.class);

            if (!catPropertyComponent.isAlive)
            {
                // respawn cat at last checkpoint
                Vector2 lastCheckpoint = catPropertyComponent.lastCheckPoint;
                float minVelocity = movementComponent.MIN_VELOCITY;
                float middleVelocity = movementComponent.MIDDLE_VELOCITY;
                float maxVelocity = movementComponent.MAX_VELOCITY;
                float acceleration = movementComponent.ACCELERATION;

                physixManager.destroy(physicsComponent.physicsBody);
                entityManager.deleteEntity(entity);

                int newPlayer = EntityFactory.constructCat(lastCheckpoint, maxVelocity, middleVelocity, minVelocity, acceleration);

                // decrease lives
                entityManager.getComponent(newPlayer, CatPropertyComponent.class).amountLives--;
            }
        }

    }

    @Override
    public void render()
    {

    }
}
