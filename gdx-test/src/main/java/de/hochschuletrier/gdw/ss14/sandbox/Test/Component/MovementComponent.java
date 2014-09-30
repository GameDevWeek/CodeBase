package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class MovementComponent implements Component{

	public float velocity;
	
	public final float ACCELERATION;
	public final float MAX_VELOCITY;
	public final float MIDDLE_VELOCITY;
	public final float MIN_VELOCITY;
	
	public Vector2 directionVec;
	
	public MovementComponent(float maxVelo, float middleVelo, float minVelo, float acceleration, Vector2 directionVec){
		MAX_VELOCITY = maxVelo;
		MIN_VELOCITY = middleVelo;
		MIDDLE_VELOCITY = minVelo;
		ACCELERATION = acceleration;
		
		this.directionVec = directionVec;
	}
	

}
