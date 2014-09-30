package de.hochschuletrier.gdw.ss14.sandbox.ecs.systems;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.commons.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class TestRenderSystem extends ECSystem
{
    private SpriteBatch spriteBatch;

    public TestRenderSystem(EntityManager entityManager)
    {
        super(entityManager, 10);
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void update(float delta)
    {
    }

    @Override
    public void render()
    {
        Array<Integer> entityArray = entityManager.getAllEntitiesWithComponents(TestRenderComponent.class, PhysicsComponent.class);

        for (Integer entity : entityArray)
        {
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);
            TestRenderComponent testRenderComponent = entityManager.getComponent(entity, TestRenderComponent.class);

            Vector2 pos = physicsComponent.getPosition();

            DrawUtil.batch.draw(testRenderComponent.texture, pos.x, pos.y);
        }

    }
}
