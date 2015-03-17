package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.BouncingComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;

public class MovementSystem extends IteratingSystem {

	public MovementSystem() {
		this(0);
	}

	public MovementSystem(int priority) {
		super(Family
				.all(PhysixBodyComponent.class)
				.one(BouncingComponent.class, JumpComponent.class,
						MovementComponent.class).get(), priority);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
		MovementComponent movement = ComponentMappers.movement.get(entity);
		BouncingComponent bouncing = ComponentMappers.bouncing.get(entity);
		JumpComponent jump = ComponentMappers.jump.get(entity);

		if (movement != null) {
			physix.simpleForceApply(movement.velocity);
		}
		if (bouncing != null) {
			// if entity is on the ground and the timeToNextBounce has surpassed
			// the current restingTime
			if (physix.getLinearVelocity().y == 0
					&& bouncing.timeToNextBounce > bouncing.restingTime) {
				physix.applyImpulse(0, bouncing.bouncingImpulse);
			}
			// if entity is on the ground, add deltaTime to timeToNextBounce
			else if (physix.getLinearVelocity().y == 0) {
				bouncing.timeToNextBounce += deltaTime;
			}
			// if entity is in the air
			else {
				bouncing.timeToNextBounce = 0;
			}
		}

		if (jump != null) {
			// if jump was called
			if (jump.doJump) {
				// if entity is on the ground and the timeToNextBounce has
				// surpassed the current restingTime --> jump!
				if (physix.getLinearVelocity().y == 0
						&& jump.timeToNextJump > jump.restingTime) {
					physix.applyImpulse(0, jump.jumpImpulse);
					// reset doJump!
					jump.doJump = false;
				}
				// if entity is on the ground, add deltaTime to timeToNextBounce
				else if (physix.getLinearVelocity().y == 0) {
					jump.timeToNextJump += deltaTime;
				}
			} else {
				jump.timeToNextJump = 0;
			}
		}
	}
}
