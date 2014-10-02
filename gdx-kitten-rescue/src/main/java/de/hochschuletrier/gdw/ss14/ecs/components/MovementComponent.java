package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component{
	
	public float velocity;
	
	public final float ACCELERATION;
	public final float DAMPING = -100f;
	public final float MAX_VELOCITY;
	public final float MIDDLE_VELOCITY;
	public final float MIN_VELOCITY;
    public final float PERCENT_OF_NEW_CAT_VELOCITY = .05f;//must 0..1 //default .05f = 5%
	
	public Vector2 directionVec;
    public Vector2 positionVec;
    public Vector2 oldPositionVec;
	
	public MovementComponent(float maxVelo, float middleVelo, float minVelo, float acceleration){
		MAX_VELOCITY = maxVelo;
		MIDDLE_VELOCITY = middleVelo;
		MIN_VELOCITY = minVelo;
		ACCELERATION = acceleration;
	}
}
