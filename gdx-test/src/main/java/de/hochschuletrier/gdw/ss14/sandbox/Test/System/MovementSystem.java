package de.hochschuletrier.gdw.ss14.sandbox.Test.System;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.CatStateComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.DogStateComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.InputComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.MovementComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.PlayerComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class MovementSystem extends ECSystem{

    public int minDistance = 50;

	public MovementSystem(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(MovementComponent.class, PhysicsComponent.class, InputComponent.class);

		for (Integer integer : compos) {
			MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
			PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
			InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
			PlayerComponent playerCompo = entityManager.getComponent(integer, PlayerComponent.class);
			CatStateComponent catStateCompo = null;
			DogStateComponent dogStateCompo = null;
			if(playerCompo != null ) {
                catStateCompo = entityManager.getComponent(integer, CatStateComponent.class);
                if(moveCompo.velocity == 0)
                    catStateCompo.state = CatStateComponent.States.STANDING;
                else if (moveCompo.velocity > 0 && moveCompo.velocity < moveCompo.MIDDLE_VELOCITY)
                    catStateCompo.state = CatStateComponent.States.WALKING;
                else if(moveCompo.velocity > moveCompo.MIDDLE_VELOCITY && moveCompo.velocity < moveCompo.MAX_VELOCITY)
                    catStateCompo.state = CatStateComponent.States.RUNNING;
            } else {
                dogStateCompo = entityManager.getComponent(integer, DogStateComponent.class);
                if(moveCompo.velocity == 0)
                    dogStateCompo.state = DogStateComponent.States.STANDING;
                else if (moveCompo.velocity > 0 && moveCompo.velocity < moveCompo.MIDDLE_VELOCITY)
                    dogStateCompo.state = DogStateComponent.States.WALKING;
                else if(moveCompo.velocity > moveCompo.MIDDLE_VELOCITY && moveCompo.velocity < moveCompo.MAX_VELOCITY)
                    dogStateCompo.state = DogStateComponent.States.RUNNING;
            }

			moveCompo.directionVec = inputCompo.whereToGo.sub(phyCompo.getPosition());

			float distance = moveCompo.directionVec.len();

			System.out.println("DISTANCE: " +  distance + " VELOCITY: " + moveCompo.velocity);

			if(distance < minDistance){
			    if(playerCompo != null)
			        catStateCompo.state = CatStateComponent.States.JUMPING;
			    else
			        dogStateCompo.state = DogStateComponent.States.KILLING;
			}

			if(distance >= 200){


				moveCompo.velocity += moveCompo.ACCELERATION * delta;

				/*
				 * Falls durch die letze Berechnung die Velocity höher als die Maximale Velocity berechnet wurde, setzen
				 * wir  unsere velocity auf MAX_VELOCITY
				 */
				if(moveCompo.velocity >= moveCompo.MAX_VELOCITY){
					moveCompo.velocity = moveCompo.MAX_VELOCITY;
				}

			}else if(distance >= 100){

				//moveCompo.velocity +=  moveCompo.ACCELERATION * delta;

				/*
				 * Falls wir von unserem Stand aus losgehen soll unsere Katze beschleunigen, bis sie "geht"
				 */
				if(moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY){
					//
					moveCompo.velocity += moveCompo.DAMPING * delta;
					if(moveCompo.velocity <= moveCompo.MIDDLE_VELOCITY){
						moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
					}
				/*
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
				//
					moveCompo.velocity +=  moveCompo.DAMPING * 1.5f * delta;
					if(moveCompo.velocity <= moveCompo.MIN_VELOCITY){
						moveCompo.velocity = 0;
					}
			}

			System.out.println(moveCompo.MIN_VELOCITY + " " + moveCompo.MIDDLE_VELOCITY + " " + moveCompo.MAX_VELOCITY);
			//Normalizing DirectionVector for Movement
			moveCompo.directionVec = moveCompo.directionVec.nor();
			float angle = (float)Math.atan2(-moveCompo.directionVec.x, moveCompo.directionVec.y);
			phyCompo.setRotation(angle);
			phyCompo.setVelocityX(moveCompo.directionVec.x * moveCompo.velocity);
			phyCompo.setVelocityY(moveCompo.directionVec.y * moveCompo.velocity);
		}

	}
}
