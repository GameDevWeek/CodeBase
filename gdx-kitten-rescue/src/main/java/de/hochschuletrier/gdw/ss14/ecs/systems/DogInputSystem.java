package de.hochschuletrier.gdw.ss14.ecs.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;

public class DogInputSystem extends ECSystem
{
    
    Logger logger = LoggerFactory.getLogger(DogInputSystem.class);
    
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
