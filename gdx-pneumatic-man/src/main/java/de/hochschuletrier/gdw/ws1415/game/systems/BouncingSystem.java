package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.BouncingComponent;

public class BouncingSystem extends IteratingSystem {

	public BouncingSystem() {
		this(0);
	}

	public BouncingSystem(int priority) {
		super(Family.all(PhysixBodyComponent.class, BouncingComponent.class)
				.get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
		BouncingComponent bouncing = ComponentMappers.bouncing.get(entity);

		if (physix.getLinearVelocity().y == 0
				&& bouncing.timeToNextBounce > bouncing.restingTime) {
			physix.applyImpulse(0, bouncing.bouncingImpulse);
		} else if (physix.getLinearVelocity().y == 0) {
			bouncing.timeToNextBounce += deltaTime;
		} else {
			bouncing.timeToNextBounce = 0;
		}
	}

}
