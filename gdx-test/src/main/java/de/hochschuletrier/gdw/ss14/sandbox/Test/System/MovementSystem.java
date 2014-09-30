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
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(MovementComponent.class, PhysicsComponent.class, InputComponent.class);
		
		for (Integer integer : compos) {
			MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
			PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
			InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
			
			moveCompo.directionVec = inputCompo.whereToGo.sub(phyCompo.getPosition());
			
			float distance = moveCompo.directionVec.len();
			
			System.out.println(distance);
			
			if(distance >= 500){
				
				moveCompo.velocity += moveCompo.ACCELERATION * delta;
				
				if(moveCompo.velocity >= moveCompo.MAX_VELOCITY){
					moveCompo.velocity = moveCompo.MAX_VELOCITY;
				}
				
				System.out.println(">= 500 VELO: " + moveCompo.velocity);
				
				
			}else if(distance >= 100){
				if(moveCompo.velocity > moveCompo.MIDDLE_VELOCITY){
					moveCompo.velocity -= moveCompo.ACCELERATION * delta;
					if(moveCompo.velocity <= moveCompo.MIDDLE_VELOCITY){
						moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
					}
				}else if(moveCompo.velocity < moveCompo.MIDDLE_VELOCITY){
					moveCompo.velocity +=  moveCompo.ACCELERATION * delta;
					if(moveCompo.velocity >= moveCompo.MIDDLE_VELOCITY){
						moveCompo.velocity = moveCompo.MIDDLE_VELOCITY;
					}
				}
				
				System.out.println(">= 100 VELO: " + moveCompo.velocity);
			}else{
					moveCompo.velocity -=  moveCompo.ACCELERATION * delta;
					if(moveCompo.velocity <= moveCompo.MIN_VELOCITY){
						moveCompo.velocity = 0;
					}
					System.out.println("< 100 VELO: " + moveCompo.velocity);
			}
			
			//Normalizing DirectionVector for Movement
			moveCompo.directionVec = moveCompo.directionVec.nor();
			phyCompo.setVelocityX(moveCompo.directionVec.x * moveCompo.velocity);
			phyCompo.setVelocityY(moveCompo.directionVec.y * moveCompo.velocity);
		}
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
}
