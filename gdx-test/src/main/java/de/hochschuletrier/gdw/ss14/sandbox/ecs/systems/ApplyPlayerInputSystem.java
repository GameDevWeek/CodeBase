package de.hochschuletrier.gdw.ss14.sandbox.ecs.systems;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.*;

/**
 * Created by Dani on 30.09.2014.
 */
public class ApplyPlayerInputSystem extends ECSystem
{
    public ApplyPlayerInputSystem(EntityManager entityManager)
    {
        super(entityManager, 6);
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(TestInputComponent.class, PhysicsComponent.class);

        for (Integer entity : entities)
        {
            TestInputComponent testInputComponent = entityManager.getComponent(entity, TestInputComponent.class);
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);

            Vector2 direction = testInputComponent.target.sub(physicsComponent.getPosition());

            physicsComponent.setVelocity(direction);
        }
    }

    @Override
    public void render()
    {

    }
}
