package de.hochschuletrier.gdw.ss14.sandbox.Test;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class MovementSystem extends ECSystem{

	public MovementSystem(EntityManager entityManager) {
		super(entityManager);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(MovementComponent.class, PositionComponent.class);
		
		for (Integer integer : compos) {
			MovementComponent moveCompo = entityManager.getComponent(integer, MovementComponent.class);
			PositionComponent posCompo = entityManager.getComponent(integer, PositionComponent.class);
			
			posCompo.position.x += moveCompo.directionVec.x * moveCompo.velocity * delta;
			posCompo.position.y += moveCompo.directionVec.y * moveCompo.velocity * delta;
		}
	}
}
