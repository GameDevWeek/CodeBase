package de.hochschuletrier.gdw.ss14.sandbox.Test.System;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.InputComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.MovementComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class MovementSystem extends ECSystem{

	public MovementSystem(EntityManager entityManager) {
		super(entityManager);
	}

	@Override
	public void update(float delta) {
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(MovementComponent.class, PhysicsComponent.class, InputComponent.class);
		
		for (Integer integer : compos) {
			MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
			PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
			InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
			
			moveCompo.directionVec = inputCompo.whereToGo.sub(phyCompo.getPosition());
			
			float distance = moveCompo.directionVec.len();
			
			if(distance >= 200){
				
				moveCompo.velocity += moveCompo.ACCELERATION * delta;
				
				/**
				 * Falls durch die letze Berechnung die Velocity höher als die Maximale Velocity berechnet wurde, setzen
				 * wir  unsere velocity auf MAX_VELOCITY
				 */
				if(moveCompo.velocity >= moveCompo.MAX_VELOCITY){
					moveCompo.velocity = moveCompo.MAX_VELOCITY;
				}
			}else if(distance >= 100){
				//moveCompo.velocity +=  moveCompo.ACCELERATION * delta;
				/**
				 * Falls wir von unserem Stand aus losgehen soll unsere Katze beschleunigen, bis sie "geht"
				 */
				if(moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY){
					//
					moveCompo.velocity += moveCompo.DAMPING * delta;
					if(moveCompo.velocity <= moveCompo.MIDDLE_VELOCITY){
						moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
					}
				/**
				 * Falls unsere Katze aus dem "Rennen" aus zu nah an unseren Laserpointer kommt, soll 
				 * sie stetig langsamer werden
				 */
				}else if(moveCompo.velocity < moveCompo.MIDDLE_VELOCITY){
					moveCompo.velocity +=  moveCompo.ACCELERATION * delta;
					if(moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY){
						moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
					}
				}
				
			}else{
                moveCompo.velocity +=  moveCompo.DAMPING * 1.5f * delta;
                if(moveCompo.velocity <= moveCompo.MIN_VELOCITY){
                    moveCompo.velocity = 0;
                }
			}

			//Normalizing DirectionVector for Movement
			moveCompo.directionVec = moveCompo.directionVec.nor();
			float angle = (float)Math.atan2(-moveCompo.directionVec.x, moveCompo.directionVec.y);
			phyCompo.setRotation(angle);
			phyCompo.setVelocityX(moveCompo.directionVec.x * moveCompo.velocity);
			phyCompo.setVelocityY(moveCompo.directionVec.y * moveCompo.velocity);
		}
	}

	@Override
	public void render() {
    }
}
