package de.hochschuletrier.gdw.ss14.sandbox.Test;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.BallPhysicsComponent;

public class EntityFactory {
	
	public static EntityManager manager;
	public static PhysixManager phyManager;
	
	public EntityFactory(){
		
	}
	public static int constructCat(Vector2 pos){
		int entity = manager.createEntity();
	    CatPhysicsComponent catPhysix = new CatPhysicsComponent((int)pos.x,(int)pos.y);
	    catPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, catPhysix);
	    return 0;
	}
	
	public static int constructDog(Vector2 pos){
		int entity = manager.createEntity();
	    CatPhysicsComponent catPhysix = new CatPhysicsComponent((int)pos.x,(int)pos.y);
	    catPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, catPhysix);
	    return 0;
	}
	

}
