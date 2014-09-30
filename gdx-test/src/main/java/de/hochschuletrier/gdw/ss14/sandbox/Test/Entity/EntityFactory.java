package de.hochschuletrier.gdw.ss14.sandbox.Test;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;

public class EntityFactory {
	
	public static EntityManager manager;
	public static PhysixManager phyManager;
	
	public EntityFactory(){
		
	}
	public static void constructCat(Vector2 pos){
		int entity = manager.createEntity();
	    CatPhysicsComponent catPhysix = new CatPhysicsComponent();
	    PositionComponent catPosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
	    MovementComponent catMove = new MovementComponent(10,3,0,1.2f,new Vector2(0,0));
	    catPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, catPhysix);
	    manager.addComponent(entity, catMove);
	}
	
	public static void constructDog(Vector2 pos){
		int entity = manager.createEntity();
		DogPhysicsComponent dogPhysix = new DogPhysicsComponent();
		dogPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, dogPhysix);
	    PositionComponent catPosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
	    MovementComponent catMove = new MovementComponent(9,4,0,1.2f,new Vector2(0,0));
	}
	

}
