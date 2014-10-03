package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.states.*;

/**
 * Created by Daniel Dreher on 03.10.2014.
 */
public class CatJumpUpdateSystem extends ECSystem
{
    public CatJumpUpdateSystem(EntityManager entityManager)
    {
        super(entityManager);
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, JumpDataComponent.class, CatPropertyComponent.class);

        for (Integer entity : entities)
        {
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
            JumpDataComponent jumpDataComponent = entityManager.getComponent(entity, JumpDataComponent.class);

            if (catPropertyComponent.getState() == CatStateEnum.JUMP)
            {
                if (jumpDataComponent.currentJumpTime < jumpDataComponent.maxJumpTime)
                {
                    jumpDataComponent.currentJumpTime += delta;
                }
            }
            else
            {
                jumpDataComponent.currentJumpTime = 0.0f;
            }
        }
    }

    @Override
    public void render()
    {

    }
}
