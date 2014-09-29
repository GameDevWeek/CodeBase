package de.hochschuletrier.gdw.ss14.sandbox.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Cat{
	
	private final int HEIGHT = 0;
	private final int WIDTH = 0;
	private boolean isAlive;
	private int thirstBar = 0;
	private Vector2 position;
	
	private Vector2 laserpointerPosition;
	private boolean isLaserpointerMoving;
	private boolean laserpointerIsOn;
	
	private int lifes = 9;
	
	private float velocity;
	
	private final float MAX_VELOCITY = 10;
	private final float MIDDLE_VELOCITY = 3;
	private final float MIN_VELOCITY = 0;
	
	private final float ACCELERATION = 1.2f;
	
	private float distanceToLaser;
	
	
	public Cat(){
		
	}
	
	public void init(){
		
	}
	
	public void update(float delta){
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		laserpointerPosition = new Vector2(x,y);
		distanceToLaser = laserpointerPosition.dst(position);
		if(distanceToLaser >= 5){
			if(velocity < MAX_VELOCITY){
				velocity += ACCELERATION * delta;
				if(velocity >= MAX_VELOCITY){
					velocity = MAX_VELOCITY;
				}
			}
			
			
		}else if(distanceToLaser >= 3){
			if(velocity > MIDDLE_VELOCITY){
				velocity -= ACCELERATION * delta;
				if(velocity <= MIDDLE_VELOCITY){
					velocity = MIDDLE_VELOCITY;
				}
			}else if(velocity < MIDDLE_VELOCITY){
				velocity +=  ACCELERATION * delta;
				if(velocity >= MIDDLE_VELOCITY){
					velocity = MIDDLE_VELOCITY;
				}
			}
			
			
		}else{
			if(velocity != MIN_VELOCITY){
				velocity -=  ACCELERATION * delta;
				if(velocity <= 0){
					velocity = MIN_VELOCITY;
				}
			}
		}
		Vector2 directionVec = new Vector2(laserpointerPosition.x, laserpointerPosition.y).sub(position).nor();
		position.x += directionVec.x*velocity*delta;
		position.y += directionVec.y*velocity*delta;
	}
	
//	public float calculateDistance(Vector2 vec1, Vector2 vec2){
//		float x = vec2.x - vec1.x;
//		float y = vec2.y - vec1.y;
//		
//		float distance = (float) Math.sqrt((x*x) + (y*y));
//		
//		return distance;
//	}

}
