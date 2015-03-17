package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;
//import de.hochschuletrier.gdw.ws1415.game.components.BouncingComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;

public class MovementSystem extends IteratingSystem {

    private static final float EPSILON = 1f/64f;

    public MovementSystem() {
        this(0);
    }

    public MovementSystem(int priority) {
        super(Family.all(PhysixBodyComponent.class, InputComponent.class)
                .one(MovementComponent.class, JumpComponent.class).get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        InputComponent input = ComponentMappers.input.get(entity);
        MovementComponent movement = ComponentMappers.movement.get(entity);
        // BouncingComponent bouncing = ComponentMappers.bouncing.get(entity);
        JumpComponent jump = ComponentMappers.jump.get(entity);

        if (movement != null) {

            if(input.direction == -1){
                movement.velocity.set(-movement.speed, movement.velocity.y);
            }else if(input.direction == 1){
                movement.velocity.set(movement.speed, movement.velocity.y);
            }else {
                movement.velocity.set(0, movement.velocity.y);
            }
            
            physix.setLinearVelocity(movement.velocity.x * deltaTime,
                    physix.getLinearVelocity().y
                            + (movement.velocity.y * deltaTime));
        }
        /*
         * deprecated if (bouncing != null) { // if entity is on the ground and
         * the timeToNextBounce has surpassed // the current restingTime if
         * (physix.getLinearVelocity().y < EPSILON &&
         * physix.getLinearVelocity().y > -EPSILON && bouncing.timeToNextBounce
         * > bouncing.restingTime) { physix.applyImpulse(0,
         * bouncing.bouncingImpulse); } // if entity is on the ground, add
         * deltaTime to timeToNextBounce else if (physix.getLinearVelocity().y <
         * EPSILON && physix.getLinearVelocity().y > -EPSILON) {
         * bouncing.timeToNextBounce += deltaTime; } // if entity is in the air
         * else { bouncing.timeToNextBounce = 0; } }
         */
        if (jump != null) {
            // if jump was called
            if (input.jump) {
                jump.doJump = true;
                // if entity is on the ground and the timeToNextBounce has
                // surpassed the current restingTime --> jump!
                if (physix.getLinearVelocity().y < EPSILON
                        && physix.getLinearVelocity().y > -EPSILON
                        && jump.timeToNextJump > jump.restingTime) {

                    physix.applyImpulse(0, jump.jumpImpulse);
                    // reset doJump!
                    jump.doJump = false;
                }
                // if entity is on the ground, add deltaTime to timeToNextBounce
                else if (physix.getLinearVelocity().y == 0) {
                    jump.timeToNextJump += deltaTime;
                }else{
                    jump.timeToNextJump = 0;
                }
            } else {
                jump.timeToNextJump = 0;
            }
        }
    }
}
