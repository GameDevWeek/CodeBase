package de.hochschuletrier.gdw.ss14.ecs.systems;

import de.hochschuletrier.gdw.ss14.ecs.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.physics.PhysicsActions;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class PlayerMovementSystem extends ECSystem{
    Logger logger = LoggerFactory.getLogger(PlayerMovementSystem.class);

    public float maxVelocity = 0, acceleration = 0;

    public PlayerMovementSystem(EntityManager entityManager){
        super(entityManager, 1);
    }

    @Override
    public void render(){
    }

    @Override
    public void update(float delta){
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, MovementComponent.class, PhysicsComponent.class, InputComponent.class, CatPropertyComponent.class);
        Array<Integer> laser = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);

        LaserPointerComponent laserPointerComponent = null;

        if(laser.size > 0)
        {
            laserPointerComponent = entityManager.getComponent(laser.first(), LaserPointerComponent.class);
        }

        for(Integer integer : compos){
            MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
            PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            CatPropertyComponent catStateCompo = entityManager.getComponent(integer, CatPropertyComponent.class);

            //Wenn Katze positive Nahrung gegessen hat, wird sie schneller
            if(maxVelocity == 0){
                maxVelocity = moveCompo.maxVelocity;
                acceleration = moveCompo.acceleration;
            }

            moveCompo.maxVelocity = maxVelocity;
            moveCompo.acceleration = acceleration;

            if(catStateCompo.atePositiveFood){
                moveCompo.maxVelocity *= 5;

                moveCompo.acceleration *= 5;

            }

            // update states
            if(moveCompo.velocity == 0){
                catStateCompo.state = CatStateEnum.IDLE;
            }else if(moveCompo.velocity > 0 && moveCompo.velocity < moveCompo.middleVelocity){
                catStateCompo.state = CatStateEnum.WALK;
                catStateCompo.timeTillJump = 0;
            }else if(moveCompo.velocity > moveCompo.middleVelocity && moveCompo.velocity < moveCompo.maxVelocity){
                catStateCompo.state = CatStateEnum.RUN;
                catStateCompo.timeTillJump = 0;
            }
            SoundManager.performAction(PhysicsActions.CATWALK);

            logger.debug("\n"+catStateCompo.state);
            if(catStateCompo.state != CatStateEnum.JUMP){
                 moveCompo.directionVec = inputCompo.whereToGo.sub(phyCompo.getPosition());
            }
            moveCompo.positionVec = moveCompo.directionVec;
            Vector2 backup = moveCompo.directionVec;

            //Katze springt, wenn nah genug an Laserpointer
            float distance = moveCompo.directionVec.len();

            if(distance <= 70 && (catStateCompo.state == CatStateEnum.IDLE)){
                catStateCompo.timeTillJump += delta;
                if(catStateCompo.timeTillJump >= 0.5){
                    catStateCompo.state = CatStateEnum.JUMP;
                    // cat is jumping - disable all puddles (blood and water)
                }
            }
            {   //sets puddles 
                boolean flag = true;
                if (catStateCompo.state == CatStateEnum.JUMP) {
                    flag = false;
                }
                Array<Integer> puddles = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
                for (Integer p : puddles) {
                    PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                    if (!(puddlecompo instanceof PuddlePhysicsComponent))
                        continue;
                    puddlecompo.physicsBody.setActive(flag);
                }
            }

            if(distance >= 200){
                moveCompo.velocity += moveCompo.acceleration*delta;

                /*
                 * Falls durch die letze Berechnung die Velocity höher als die Maximale Velocity berechnet wurde, setzen
                 * wir  unsere velocity auf MAX_VELOCITY
                 */
                if(moveCompo.velocity >= moveCompo.maxVelocity){
                    moveCompo.velocity = moveCompo.maxVelocity;
                }
            }else if(distance >= 100){
                //moveCompo.velocity +=  moveCompo.ACCELERATION * delta;

                /**
                 * Falls wir von unserem Stand aus losgehen soll unsere Katze beschleunigen, bis sie "geht"
                 */
                if(moveCompo.velocity >= moveCompo.middleVelocity){
                    moveCompo.velocity += moveCompo.damping*delta;
                    if(moveCompo.velocity <= moveCompo.middleVelocity){
                        moveCompo.velocity = moveCompo.middleVelocity;
                    }
                    /**
                     * Falls unsere Katze aus dem "Rennen" aus zu nah an unseren Laserpointer kommt, soll
                     * sie stetig langsamer werden
                     */
                }else if(moveCompo.velocity < moveCompo.middleVelocity){
                    moveCompo.velocity += moveCompo.acceleration*delta;
                    if(moveCompo.velocity >= moveCompo.middleVelocity){
                        moveCompo.velocity = moveCompo.middleVelocity;
                    }
                }
            }else if(catStateCompo.state == CatStateEnum.JUMP){
                moveCompo.velocity = 200;
                catStateCompo.timeTillJump = 0;
                catStateCompo.timeWhileJump += delta;
                if(catStateCompo.timeWhileJump >= 1){
                    catStateCompo.state = CatStateEnum.IDLE;
                }
                // phyCompo.setRotation(phyCompo.getRotation());
            }else{
                moveCompo.velocity += moveCompo.damping*1.5f*delta;
                if(moveCompo.velocity <= moveCompo.minVelocity){
                    moveCompo.velocity = 0;
                }
            }

            if(moveCompo.oldPositionVec == null){
                moveCompo.oldPositionVec = moveCompo.positionVec;
            }
            moveCompo.positionVec.x = (moveCompo.positionVec.x*moveCompo.percentOfNewCatVelocity+moveCompo.oldPositionVec.x*(1-moveCompo.percentOfNewCatVelocity));
            moveCompo.positionVec.y = (moveCompo.positionVec.y*moveCompo.percentOfNewCatVelocity+moveCompo.oldPositionVec.y*(1-moveCompo.percentOfNewCatVelocity));
            moveCompo.oldPositionVec.x = backup.x;
            moveCompo.oldPositionVec.y = backup.y;

            float angle;
            //Normalizing DirectionVector for Movement
            moveCompo.directionVec = moveCompo.directionVec.nor();
            moveCompo.positionVec = moveCompo.positionVec.nor();

            if(catStateCompo.state == CatStateEnum.JUMP){
                angle = phyCompo.getRotation();
            }else{
                angle = (float) Math.atan2(-moveCompo.directionVec.x, moveCompo.directionVec.y);
            }

            if(laserPointerComponent != null)
            {
                if(!laserPointerComponent.isVisible)
                {
                    moveCompo.velocity = 0.0f;
                }

                if(!catStateCompo.isHidden && laserPointerComponent.isVisible && catStateCompo.state != CatStateEnum.JUMP)
                {
                    phyCompo.setRotation(angle);
                }
            }



            phyCompo.setVelocityX(moveCompo.positionVec.x*moveCompo.velocity);
            phyCompo.setVelocityY(moveCompo.positionVec.y*moveCompo.velocity);
            logger.debug("\n"+catStateCompo.timeTillJump);
        }

    }
} 
