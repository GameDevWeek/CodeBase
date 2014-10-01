package de.hochschuletrier.gdw.ss14.sandbox.Test.System;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.EnemyComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.InputComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.PlayerComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class DogInputSystem extends ECSystem{

	public DogInputSystem(EntityManager entityManager) {
		super(entityManager, 1);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, EnemyComponent.class);
		Array<Integer> compos2 = entityManager.getAllEntitiesWithComponents(PlayerComponent.class);
		
		for (Integer integer : compos) {
			InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
			PhysicsComponent phyCompo = entityManager.getComponent(compos2.get(0), PhysicsComponent.class);
			inputCompo.whereToGo = phyCompo.getPosition();
		}
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

}
