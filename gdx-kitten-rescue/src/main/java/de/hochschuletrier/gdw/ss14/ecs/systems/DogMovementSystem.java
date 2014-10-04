package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;
import de.hochschuletrier.gdw.ss14.states.DogStateEnum;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class DogMovementSystem extends ECSystem
{
    public DogMovementSystem(EntityManager entityManager)
    {
        super(entityManager, 2);
    }

    @Override
    public void render()
    {

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
            else if (movementComponent.velocity > 0 && movementComponent.velocity < movementComponent.middleVelocity)
            {
                dogPropertyComponent.state = DogStateEnum.WALKING;
            }
            else if (movementComponent.velocity > movementComponent.middleVelocity && movementComponent.velocity < movementComponent.maxVelocity)
            {
                dogPropertyComponent.state = DogStateEnum.RUNNING;
            }

            // TODO: properly set dog velocity here
            movementComponent.velocity += movementComponent.acceleration * delta;

            if (movementComponent.velocity > movementComponent.maxVelocity)
            {
                movementComponent.velocity = movementComponent.maxVelocity;
            }

            Vector2 directionVector = new Vector2();
            directionVector.x = inputComponent.whereToGo.x - physicsComponent.getPosition().x;
            directionVector.y = inputComponent.whereToGo.y - physicsComponent.getPosition().y;

            float distance = directionVector.len();

            if(distance <= 30){
                dogPropertyComponent.state = DogStateEnum.KILLING;
                SoundManager.performAction(DogStateEnum.KILLING);
            }

            // DON'T use sub-method of vector! (causes some strange bugs!)
            //movementComponent.directionVec = inputComponent.whereToGo.sub(physicsComponent.getPosition());
            movementComponent.directionVec = directionVector;

            //Normalizing DirectionVector for Movement
            movementComponent.directionVec = movementComponent.directionVec.nor();
            float angle = (float) Math.atan2(-movementComponent.directionVec.x, movementComponent.directionVec.y);
            physicsComponent.setRotation(angle);
            physicsComponent.setVelocityX(movementComponent.directionVec.x * movementComponent.velocity);
            physicsComponent.setVelocityY(movementComponent.directionVec.y * movementComponent.velocity);
        }
    }
}
