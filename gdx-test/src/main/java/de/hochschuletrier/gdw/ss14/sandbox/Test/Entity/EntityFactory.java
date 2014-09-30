package de.hochschuletrier.gdw.ss14.sandbox.Test.Entity;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.DogPhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.HolePhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.MovementComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.PositionComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;

public class EntityFactory {
	
	public static EntityManager manager;
	public static PhysixManager phyManager;
	
	public EntityFactory(){
		
	}
	public static void constructCat(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
		int entity = manager.createEntity();
	    CatPhysicsComponent catPhysix = new CatPhysicsComponent();
	    PositionComponent catPosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
	    MovementComponent catMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration,new Vector2(0,0));
	    catPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, catPhysix);
	    manager.addComponent(entity, catPosition);
	    manager.addComponent(entity, catMove);
	}
	
	public static void constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
		int entity = manager.createEntity();
		DogPhysicsComponent dogPhysix = new DogPhysicsComponent();
		PositionComponent dogPosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
	    MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration,new Vector2(0,0));
		dogPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, dogPhysix);
	    manager.addComponent(entity, dogPosition);
	    manager.addComponent(entity, dogMove);
	}
	
	public static void constructHole(Vector2 pos){
		int entity = manager.createEntity();
		HolePhysicsComponent holePhysix = new HolePhysicsComponent();
		PositionComponent holePosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
		holePhysix.initPhysics(phyManager);
	    manager.addComponent(entity, holePhysix);
	    manager.addComponent(entity, holePosition);
	}
	
//	public static void constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
//		int entity = manager.createEntity();
//		DogPhysicsComponent dogPhysix = new DogPhysicsComponent();
//		PositionComponent dogPosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
//	    MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration,new Vector2(0,0));
//		dogPhysix.initPhysics(phyManager);
//	    manager.addComponent(entity, dogPhysix);
//	    manager.addComponent(entity, dogPosition);
//	    manager.addComponent(entity, dogMove);
//	}
	

}
