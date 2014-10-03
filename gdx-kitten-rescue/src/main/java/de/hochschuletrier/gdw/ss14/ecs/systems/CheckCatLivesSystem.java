package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

/**
 * Created by Daniel Dreher on 02.10.2014.
 */
public class CheckCatLivesSystem extends ECSystem
{
    public CheckCatLivesSystem(EntityManager entityManager)
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

            if (catPropertyComponent.amountLives <= 0)
            {
                // TODO: insert game over stuff here
            }
        }

    }

    @Override
    public void render()
    {

    }
}
