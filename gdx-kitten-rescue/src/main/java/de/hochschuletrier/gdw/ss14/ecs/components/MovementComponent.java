package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component{

	public float velocity;
    public float acceleration;
    public float damping = -100f;
    public float maxVelocity;
    public float middleVelocity;
    public float minVelocity;
	
	public Vector2 directionVec;
    public Vector2 positionVec;
    public Vector2 oldPositionVec;

	public MovementComponent(float maxVelo, float middleVelo, float minVelo, float acceleration){
		maxVelocity = maxVelo;
		middleVelocity = middleVelo;
		minVelocity = minVelo;
		this.acceleration = acceleration;

        directionVec = new Vector2();
	}
}
