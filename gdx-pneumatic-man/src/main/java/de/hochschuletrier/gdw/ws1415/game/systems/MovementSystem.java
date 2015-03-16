package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;

public class MovementSystem extends IteratingSystem{
	
	public MovementSystem(){
		this(0);
	}

	public MovementSystem(int priority) {
		super(Family.all(PhysixBodyComponent.class, MovementComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
		MovementComponent movement = ComponentMappers.movement.get(entity);
		
		physix.simpleForceApply(movement.velocity);
	}

	

}
