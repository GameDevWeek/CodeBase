package Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Cat{
	
	private final int HEIGHT = 0;
	private final int WIDTH = 0;
	private boolean isAlive;
	private int thirstBar = 0;
	private Vector2 position;
	private Vector2 laserpointerPosition;
	
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
		distanceToLaser = calculateDistance(position, laserpointerPosition);
		if(distanceToLaser >= 5){
			if(velocity != MAX_VELOCITY){
				velocity += velocity * ACCELERATION;
			}
		}else if(distanceToLaser >= 3){
			if(velocity >= MIDDLE_VELOCITY){
				velocity -= velocity * ACCELERATION;
			}else{
				velocity += velocity * ACCELERATION;
			}
		}else{
			if(velocity != 0){
				velocity -= velocity * ACCELERATION;
			}
		}
		Vector2 directionVec = new Vector2(laserpointerPosition.x - position.x, laserpointerPosition.y - position.y);
		position.x += directionVec.x + velocity*delta;
		position.y += directionVec.y + velocity*delta;
	}
	
	public float calculateDistance(Vector2 vec1, Vector2 vec2){
		float x = vec2.x - vec1.x;
		float y = vec2.y - vec1.y;
		
		float distance = (float) Math.sqrt((x*x) + (y*y));
		
		return distance;
	}
	
	public void runTo(Vector2 position){
		
	}
	

}
