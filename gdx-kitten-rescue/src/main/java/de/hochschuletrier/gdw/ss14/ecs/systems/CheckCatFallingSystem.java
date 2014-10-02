package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.states.*;

/**
 * Created by Daniel Dreher on 02.10.2014.
 */
public class CheckCatFallingSystem extends ECSystem
{
    public CheckCatFallingSystem(EntityManager entityManager)
    {
        super(entityManager, 4);
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class, AnimationComponent.class, MovementComponent.class);

        for (Integer entity : entities)
        {
            AnimationComponent animationComponent = entityManager.getComponent(entity, AnimationComponent.class);
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
            MovementComponent movementComponent = entityManager.getComponent(entity, MovementComponent.class);

            if(catPropertyComponent.state == CatStateEnum.FALL)
            {
                // check if animation is finished here

            }

        }

    }

    @Override
    public void render()
    {

    }
}
