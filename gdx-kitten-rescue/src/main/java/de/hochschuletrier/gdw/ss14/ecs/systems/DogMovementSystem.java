package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.states.*;
import org.slf4j.*;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class DogMovementSystem extends ECSystem
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DogMovementSystem.class);

    public DogMovementSystem(EntityManager entityManager)
    {
        super(entityManager, 2);

    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(MovementComponent.class, PhysicsComponent.class, InputComponent.class, DogPropertyComponent.class);

        for (Integer entity : entities)
        {
            MovementComponent movementComponent = entityManager.getComponent(entity, MovementComponent.class);
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);
            InputComponent inputComponent = entityManager.getComponent(entity, InputComponent.class);
            DogPropertyComponent dogPropertyComponent = entityManager.getComponent(entity, DogPropertyComponent.class);

            // update states
            if (movementComponent.velocity == 0)
            {
                dogPropertyComponent.state = DogStateEnum.SITTING;
            }
            else if (movementComponent.velocity > 0 && movementComponent.velocity < movementComponent.MIDDLE_VELOCITY)
            {
                dogPropertyComponent.state = DogStateEnum.WALKING;
            }
            else if (movementComponent.velocity > movementComponent.MIDDLE_VELOCITY && movementComponent.velocity < movementComponent.MAX_VELOCITY)
            {
                dogPropertyComponent.state = DogStateEnum.RUNNING;
            }

            // TODO: properly set dog velocity here
            movementComponent.velocity += movementComponent.ACCELERATION * delta;

            if (movementComponent.velocity > movementComponent.MAX_VELOCITY)
            {
                movementComponent.velocity = movementComponent.MAX_VELOCITY;
            }

            movementComponent.directionVec = inputComponent.whereToGo.sub(physicsComponent.getPosition());

            //Normalizing DirectionVector for Movement
            movementComponent.directionVec = movementComponent.directionVec.nor();
            float angle = (float) Math.atan2(-movementComponent.directionVec.x, movementComponent.directionVec.y);
            physicsComponent.setRotation(angle);
            physicsComponent.setVelocityX(movementComponent.directionVec.x * movementComponent.velocity);
            physicsComponent.setVelocityY(movementComponent.directionVec.y * movementComponent.velocity);

            logger.debug
                    ("\n"
                                    + "Dog-ID: (" + entity + ")\n"
                                    + "Target: (" + inputComponent.whereToGo.x + ", " + inputComponent.whereToGo.y + ")\n"
                    );
        }
    }

    @Override
    public void render()
    {

    }
}
