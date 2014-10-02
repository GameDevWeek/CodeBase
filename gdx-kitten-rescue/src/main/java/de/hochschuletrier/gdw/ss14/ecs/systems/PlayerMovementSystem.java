package de.hochschuletrier.gdw.ss14.ecs.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class PlayerMovementSystem extends ECSystem
{
    
    Logger logger = LoggerFactory.getLogger(PlayerMovementSystem.class);
    
    public PlayerMovementSystem(EntityManager entityManager)
    {
        super(entityManager, 1);
    }

    @Override
    public void render()
    {

    }

    @Override
    public void update(float delta)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, MovementComponent.class, PhysicsComponent.class, InputComponent.class, CatPropertyComponent.class);

        for (Integer integer : compos)
        {
            MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
            PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            CatPropertyComponent catStateCompo = entityManager.getComponent(integer, CatPropertyComponent.class);


            // update states
            if (moveCompo.velocity == 0)
            {
                catStateCompo.state = CatStateEnum.IDLE;
            }
            else if (moveCompo.velocity > 0 && moveCompo.velocity < moveCompo.MIDDLE_VELOCITY)
            {
                catStateCompo.state = CatStateEnum.WALK;
            }
            else if (moveCompo.velocity > moveCompo.MIDDLE_VELOCITY && moveCompo.velocity < moveCompo.MAX_VELOCITY)
            {
                catStateCompo.state = CatStateEnum.RUN;
            }

            moveCompo.directionVec = inputCompo.whereToGo.sub(phyCompo.getPosition());
            float distance = moveCompo.directionVec.len();

            //Katze springt, wenn nah genug an Laserpointer
            if (distance <= 70 && (catStateCompo.state == CatStateEnum.IDLE))
            {
                catStateCompo.jumpBuffer += delta;
                if (catStateCompo.jumpBuffer >= 0.5)
                {
                    catStateCompo.state = CatStateEnum.JUMP;

                }

            }

            if (distance >= 200)
            {

                moveCompo.velocity += moveCompo.ACCELERATION * delta;

                /*
                 * Falls durch die letze Berechnung die Velocity höher als die Maximale Velocity berechnet wurde, setzen
                 * wir  unsere velocity auf MAX_VELOCITY
                 */
                if (moveCompo.velocity >= moveCompo.MAX_VELOCITY)
                {
                    moveCompo.velocity = moveCompo.MAX_VELOCITY;
                }

            }
            else if (distance >= 100)
            {

                //moveCompo.velocity +=  moveCompo.ACCELERATION * delta;

                /*
                 * Falls wir von unserem Stand aus losgehen soll unsere Katze beschleunigen, bis sie "geht"
                 */
                if (moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY)
                {
                    //
                    moveCompo.velocity += moveCompo.DAMPING * delta;
                    if (moveCompo.velocity <= moveCompo.MIDDLE_VELOCITY)
                    {
                        moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
                    }
                /*
                 * Falls unsere Katze aus dem "Rennen" aus zu nah an unseren Laserpointer kommt, soll
                 * sie stetig langsamer werden
                 */
                }
                else if (moveCompo.velocity < moveCompo.MIDDLE_VELOCITY)
                {
                    moveCompo.velocity += moveCompo.ACCELERATION * delta;
                    if (moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY)
                    {
                        moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
                    }
                }

            }
            else if (catStateCompo.state == CatStateEnum.JUMP)
            {
                moveCompo.velocity = 200;
                catStateCompo.state = CatStateEnum.IDLE;
                catStateCompo.jumpBuffer = 0;
//                phyCompo.setRotation(phyCompo.getRotation());
                if(distance <= 10){
                    moveCompo.velocity = 0;
                }
            }
            else
            {
                //
                moveCompo.velocity += moveCompo.DAMPING * 1.5f * delta;
                if (moveCompo.velocity <= moveCompo.MIN_VELOCITY)
                {
                    moveCompo.velocity = 0;
                }
            }
            
            float angle = 0;
            //Normalizing DirectionVector for Movement
            moveCompo.directionVec = moveCompo.directionVec.nor();
            
            if(catStateCompo.state == CatStateEnum.JUMP || catStateCompo.state == CatStateEnum.IDLE){
                angle = phyCompo.getRotation();
            }else{
                angle = (float) Math.atan2(-moveCompo.directionVec.x, moveCompo.directionVec.y);
            }
            
            if (!catStateCompo.canSeeLaserPointer)
            {
                moveCompo.velocity = 0.0f;
                //return;
            }
            else
            {
                phyCompo.setRotation(angle);
            }
            phyCompo.setVelocityX(moveCompo.directionVec.x * moveCompo.velocity);
            phyCompo.setVelocityY(moveCompo.directionVec.y * moveCompo.velocity);
            logger.debug("\n" + catStateCompo.jumpBuffer);
        }

    }
}
