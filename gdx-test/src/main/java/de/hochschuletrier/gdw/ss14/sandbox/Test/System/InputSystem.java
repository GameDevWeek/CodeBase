package de.hochschuletrier.gdw.ss14.sandbox.Test.System;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.InputComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.PlayerComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class InputSystem extends ECSystem{

	public InputSystem(EntityManager entityManager) {
		super(entityManager, 1);
	}

	@Override
	public void update(float delta) {
		Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, PlayerComponent.class);
		
		for (Integer integer : compos) {
			InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
			inputCompo.whereToGo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
		}
	}

	@Override
	public void render() {
	}

}
