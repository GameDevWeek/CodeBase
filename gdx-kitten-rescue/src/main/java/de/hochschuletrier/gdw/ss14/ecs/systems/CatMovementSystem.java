package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.ToolState;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

/**
 * Created by Daniel Dreher on 03.10.2014.
 */
public class CatMovementSystem extends ECSystem{

    private float maxVelocity = 0.0f, acceleration = 0.0f, foodBuffer = 0.0f;
    
    // Max spinning velocity in angle (radiants) per second
    private static final float MaxAngularVelocity = (float)(4*Math.PI); 

    public CatMovementSystem(EntityManager entityManager){
        super(entityManager, 1);
    }


    @Override
    public void render(){

    }

    @Override
    public void update(float delta){
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, MovementComponent.class, PhysicsComponent.class, InputComponent.class, CatPropertyComponent.class, JumpDataComponent.class);
        Array<Integer> laser = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);

        LaserPointerComponent laserPointerComponent = null;

        if(laser.size > 0){
            laserPointerComponent = entityManager.getComponent(laser.first(), LaserPointerComponent.class);
        }

        for(Integer entity : entities){
            MovementComponent movementComponent = entityManager.getComponent(entity, MovementComponent.class);
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);
            InputComponent inputComponent = entityManager.getComponent(entity, InputComponent.class);
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
            JumpDataComponent jumpDataComponent = entityManager.getComponent(entity, JumpDataComponent.class);

            if(catPropertyComponent.getState() == CatStateEnum.IDLE || catPropertyComponent.getState() == CatStateEnum.WALK || catPropertyComponent.getState() == CatStateEnum.RUN){
                Vector2 tmp = new Vector2(inputComponent.whereToGo.x-physicsComponent.getPosition().x, inputComponent.whereToGo.y-physicsComponent.getPosition().y);
                float distance = tmp.len();

                //falls Maus nicht zu nah an Katze (glitscht nicht mehr)
                if(!(distance <= 5)){
                    movementComponent.directionVec.x = tmp.x;
                    movementComponent.directionVec.y = tmp.y;
                }

                //Wenn Katze positive Nahrung gegessen hat, wird sie schneller

                if(maxVelocity == 0){
                    maxVelocity = movementComponent.maxVelocity;
                    acceleration = movementComponent.acceleration;
                }

                movementComponent.maxVelocity = maxVelocity;
                movementComponent.acceleration = acceleration;

                if(foodBuffer <= 3.0f){
                    if(catPropertyComponent.atePositiveFood){
                        foodBuffer += delta;
                        movementComponent.maxVelocity *= 5;

                        movementComponent.acceleration *= 5;

                    }
                }else{
                    catPropertyComponent.atePositiveFood = false;
                    foodBuffer = 0;
                }

                ////sliding stuff start////
                if(movementComponent.positionVec == null)
                    movementComponent.positionVec = new Vector2(movementComponent.directionVec.x, movementComponent.directionVec.y);
                if(movementComponent.oldPositionVec == null)
                    movementComponent.oldPositionVec = new Vector2(movementComponent.positionVec.x, movementComponent.positionVec.y);
                ////sliding stuff end////

                //test if cat sees the pointer //don't ask why this way
                catPropertyComponent.canSeeLaserPointer = false;
                if(distance < 500 && laserPointerComponent.toolState == ToolState.LASER){
                    /*float physicAngle2 = (float) Math.atan2(-(movementComponent.oldPositionVec.x-movementComponent.directionVec.x),
                            movementComponent.oldPositionVec.y-movementComponent.directionVec.y);
                    catPropertyComponent.canSeeLaserPointer = (physicAngle2 > 1.7f);
                    //if(0f == physicAngle)catPropertyComponent.canSeeLaserPointer = false;

                    System.out.print(physicAngle2+" ");*/
                    catPropertyComponent.canSeeLaserPointer = true;
                }else{
                    catPropertyComponent.canSeeLaserPointer = false;
                }

                //System.out.print(catPropertyComponent.canSeeLaserPointer);

                if(distance >= 200 && catPropertyComponent.canSeeLaserPointer){
                    movementComponent.velocity += movementComponent.acceleration*delta;

                    if(movementComponent.velocity >= movementComponent.maxVelocity){
                        movementComponent.velocity = movementComponent.maxVelocity;
                    }
                }else if(distance >= 100 && catPropertyComponent.canSeeLaserPointer){
                    /**
                     * Falls wir von unserem Stand aus losgehen soll unsere Katze beschleunigen, bis sie "geht"
                     */
                    if(movementComponent.velocity >= movementComponent.middleVelocity){
                        movementComponent.velocity += movementComponent.damping*delta;
                        if(movementComponent.velocity <= movementComponent.middleVelocity){
                            movementComponent.velocity = movementComponent.middleVelocity;
                        }
                    }
                    /**
                     * Falls unsere Katze aus dem "Rennen" aus zu nah an unseren Laserpointer kommt, soll
                     * sie stetig langsamer werden
                     */
                    else if(movementComponent.velocity < movementComponent.middleVelocity){
                        movementComponent.velocity += movementComponent.acceleration*delta;
                        if(movementComponent.velocity >= movementComponent.middleVelocity){
                            movementComponent.velocity = movementComponent.middleVelocity;
                        }
                    }

                }else if(catPropertyComponent.canSeeLaserPointer){
                    if(catPropertyComponent.isInfluenced){
                        movementComponent.velocity += movementComponent.acceleration*1.5f*delta;
                        if(movementComponent.velocity <= movementComponent.minVelocity){
                            movementComponent.velocity = 0;
                        }
                    }else{
                        movementComponent.velocity += movementComponent.damping*1.5f*delta;
                        if(movementComponent.velocity <= movementComponent.minVelocity){
                            movementComponent.velocity = 0;
                        }
                    }
                }

                if(distance <= 70 && distance >= 30){
                    if(catPropertyComponent.getState() == CatStateEnum.IDLE){
                        if(laserPointerComponent != null){
                            if(laserPointerComponent.toolState == ToolState.LASER){
                                catPropertyComponent.timeTillJumpTimer = catPropertyComponent.timeTillJumpTimer+delta;
                                if(catPropertyComponent.timeTillJumpTimer >= catPropertyComponent.TIME_TILL_JUMP){
                                    catPropertyComponent.setState(CatStateEnum.JUMP);
                                    SoundManager.performAction(CatStateEnum.JUMP);
                                    jumpDataComponent.jumpDirection = movementComponent.directionVec.nor();
                                }
                            }
                        }
                    }
                }else{
                    catPropertyComponent.timeTillJumpTimer = 0.0f;
                }

                ////sliding stuff start////
                //if next stuff fails than 100% of actual direction
                //DON'T MAKE IT 0 IN ANY CASE ! it must be at least have .0001f
                float percentNewCatVelo = 1;
                if(distance < 5){
                    //pointer to near to cat - no direction change
                    percentNewCatVelo = .0001f;
                }else if(movementComponent.velocity < movementComponent.middleVelocity){
                    //no sliding between 0 and middleVelocity
                    percentNewCatVelo = 1;
                }else if(movementComponent.velocity >= movementComponent.middleVelocity && movementComponent.velocity < movementComponent.maxVelocity){
                    //sliding depending on angel between old and actual direction
                    float physicAngle = (float) Math.atan2(-(movementComponent.oldPositionVec.x-movementComponent.positionVec.x),
                            movementComponent.oldPositionVec.y-movementComponent.positionVec.y);
                    if(physicAngle < 0)physicAngle *= -1;

                    if(physicAngle < .2f){
                        percentNewCatVelo = 1f;
                    }else if(physicAngle < .4f){
                        percentNewCatVelo = .1f;
                    }else if(physicAngle < .8f){
                        percentNewCatVelo = .05f;
                    }else{
                        percentNewCatVelo = .01f;
                        movementComponent.slidingLock = true;
                    }
                }

                if(!catPropertyComponent.canSeeLaserPointer){
                    //slow down on laserPointer of
                    movementComponent.velocity = movementComponent.velocity*(1-delta);
                    percentNewCatVelo = .0001f;
                }

                movementComponent.positionVec.x = movementComponent.directionVec.x*percentNewCatVelo+movementComponent.oldPositionVec.x*(1-percentNewCatVelo);
                movementComponent.positionVec.y = movementComponent.directionVec.y*percentNewCatVelo+movementComponent.oldPositionVec.y*(1-percentNewCatVelo);
                movementComponent.oldPositionVec.x = movementComponent.positionVec.x;
                movementComponent.oldPositionVec.y = movementComponent.positionVec.y;
                movementComponent.positionVec.nor();

                if(movementComponent.velocity < 10f){
                    movementComponent.slidingLock = false;
                }
                ////sliding stuff end////

                movementComponent.directionVec = movementComponent.directionVec.nor();

                float angle = (float) Math.atan2(-movementComponent.directionVec.x, movementComponent.directionVec.y);

                if(laserPointerComponent != null){
                    if(!catPropertyComponent.canSeeLaserPointer){
                        ////sliding stuff start////
                        if(movementComponent.velocity < 10f){
                            //slowdown on laserPointer off stops to 0 if speed < 10
                            movementComponent.velocity = 0.0f;
                        }else if(movementComponent.slidingLock){
                            //slowdown on laserPointer off with slidingLock
                            movementComponent.velocity = movementComponent.velocity - 10*delta;
                        }else if(!movementComponent.slidingLock){
                            //slowdown on laserPointer off without slidingLock
                            movementComponent.velocity = movementComponent.velocity - 100*delta;
                        }
                        ////sliding stuff end////
                    }

                    if (!catPropertyComponent.isHidden && catPropertyComponent.canSeeLaserPointer)
                    {
                        float currentRot = physicsComponent.getRotation();
                        
                        if (currentRot < 0f)
                            currentRot += (float)(2*Math.PI);
                        
                        if (angle < 0f)
                            angle += (float)(2*Math.PI);

                        // Spin into the shortest direction towards the target angle
                        float spinningAngle = angle - currentRot;
                        if (Math.abs(spinningAngle) > Math.PI)
                            spinningAngle = Math.signum(spinningAngle)*(Math.abs(spinningAngle)-(float)(2*Math.PI));
                        
                        // Clamp rotation between - and + max possible rotation
                        if (Math.abs(spinningAngle) > MaxAngularVelocity*delta)
                            spinningAngle = Math.signum(spinningAngle) * MaxAngularVelocity*delta;      
                        
                        // Clamp between -360 and +360 degrees
                        float newRotation = currentRot + spinningAngle;
                        newRotation -= Math.signum(newRotation)*(float)(2*Math.PI)*(int)(newRotation / (2*Math.PI));
                        physicsComponent.setRotation(newRotation);
                    }
                }
            } // end if (state check)
            else if(catPropertyComponent.getState() == CatStateEnum.JUMP){
                movementComponent.velocity = jumpDataComponent.jumpVelocity;
            }else if(catPropertyComponent.getState() == CatStateEnum.FALL || catPropertyComponent.getState() == CatStateEnum.DIE){
                movementComponent.velocity = 0.0f;
            }else if(catPropertyComponent.getState() == CatStateEnum.HIDDEN){
                movementComponent.velocity = 0.0f;
            }else if(catPropertyComponent.getState() == CatStateEnum.PLAYS_WITH_WOOL){
                movementComponent.velocity = 0.0f;
                if(catPropertyComponent.playTimeTimer < catPropertyComponent.PLAYTIME){
                    catPropertyComponent.playTimeTimer += delta;
                }else{
                    catPropertyComponent.playTimeTimer = 0;
                    catPropertyComponent.setState(CatStateEnum.IDLE);
                }
            }else if( catPropertyComponent.getState() ==  CatStateEnum.JUMPING_IN_BOX){
                catPropertyComponent.jumpInBoxTimer += delta;
                if(catPropertyComponent.jumpInBoxTimer >= catPropertyComponent.JUMP_IN_BOX){
                    catPropertyComponent.jumpInBoxTimer = 0;
                   // catPropertyComponent.setState(CatStateEnum.HIDDEN);
                }
            }
            //positionVec not directionVec because sliding
            physicsComponent.setVelocityX(movementComponent.positionVec.x*movementComponent.velocity);
            physicsComponent.setVelocityY(movementComponent.positionVec.y*movementComponent.velocity);
        }
    }
}
