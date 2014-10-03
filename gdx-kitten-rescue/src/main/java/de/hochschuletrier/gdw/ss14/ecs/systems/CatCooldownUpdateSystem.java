package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

/**
 * Created by Daniel Dreher on 03.10.2014.
 */
public class CatCooldownUpdateSystem extends ECSystem
{
    public CatCooldownUpdateSystem(EntityManager entityManager)
    {
        super(entityManager);
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class);

        for (Integer entity : entities)
        {
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);

            if(catPropertyComponent.isCatBoxOnCooldown)
            {
                catPropertyComponent.catBoxCooldownTimer -= delta;

                if(catPropertyComponent.catBoxCooldownTimer <= 0.0f)
                {
                    catPropertyComponent.isCatBoxOnCooldown = false;
                }

            }
        }

    }

    @Override
    public void render()
    {

    }
}
