package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

public class DogInputSystem extends ECSystem
{

    public DogInputSystem(EntityManager entityManager)
    {
        super(entityManager, 1);
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> dogEntities = entityManager.getAllEntitiesWithComponents(InputComponent.class, EnemyComponent.class);
        Array<Integer> playerEntities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);

        if (playerEntities.first() != null)
        {
            int player = playerEntities.first();

            for (Integer entity : dogEntities)
            {
                InputComponent inputComponent = entityManager.getComponent(entity, InputComponent.class);
                PhysicsComponent physicsComponent = entityManager.getComponent(player, PhysicsComponent.class);
                inputComponent.whereToGo = physicsComponent.getPosition();
            }
        }
    }

    @Override
    public void render()
    {
    }
}
