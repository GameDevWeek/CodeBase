package de.hochschuletrier.gdw.ss14.sandbox.ecs.systems;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.*;

import java.util.*;

/**
 * Created by Dani on 30.09.2014.
 */
public class TestInputUpdateSystem extends ECSystem
{
    public TestInputUpdateSystem(EntityManager entityManager)
    {
        super(entityManager, 0);
    }

    @Override
    public void update(float delta)
    {
        List components = entityManager.getAllComponentsOfType(TestInputComponent.class);
        //Array<Integer> entities = entityManager.getAllEntitiesWithComponents(TestInputComponent.class);


        for (int i = 0; i < components.size(); ++i)
        {
            TestInputComponent testInputComponent = (TestInputComponent) components.get(i);

            testInputComponent.target = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        }
    }

    @Override
    public void render()
    {

    }
}
