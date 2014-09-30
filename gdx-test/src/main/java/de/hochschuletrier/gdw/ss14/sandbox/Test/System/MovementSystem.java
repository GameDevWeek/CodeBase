package de.hochschuletrier.gdw.ss14.sandbox.Test.System;

import com.badlogic.gdx.utils.Array;

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
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(MovementComponent.class);
		
		for (Integer integer : compos) {
			MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
			PhysicsComponent phyCompo = entityManager.getComponent(integer, PhysicsComponent.class);
			//Normalizing DirectionVector for Movement
			moveCompo.directionVec = moveCompo.directionVec.nor();
			float x = phyCompo.physicsBody.getX() + moveCompo.directionVec.x * moveCompo.velocity * delta;
			phyCompo.physicsBody.setX(x);
			float y = phyCompo.physicsBody.getY() + moveCompo.directionVec.y * moveCompo.velocity * delta;
			phyCompo.physicsBody.setX(y);
			System.out.println(phyCompo.physicsBody.getLinearVelocity());
		}
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
}
