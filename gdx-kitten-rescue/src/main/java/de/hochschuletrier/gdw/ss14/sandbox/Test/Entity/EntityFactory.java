package de.hochschuletrier.gdw.ss14.sandbox.Test.Entity;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.HolePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.CatPhysicsComponent;

public class EntityFactory {
	
	public static EntityManager manager;
	public static PhysixManager phyManager;
	public static AssetManagerX assetManager;
	
	public EntityFactory(EntityManager manager, PhysixManager phyManager, AssetManagerX assetManager){
		this.manager = manager;
		this.phyManager = phyManager;
		this.assetManager = assetManager;
	}
	
	public static void constructCat(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
		int entity = manager.createEntity();
		System.err.println("create cat");
	    CatPhysicsComponent catPhysix = new CatPhysicsComponent();
	    //PositionComponent catPosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
	    MovementComponent catMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
	   //	PhysicsComponent catPhysix = new PhysicsComponent();
	    catPhysix.initPhysics(phyManager);
	    catPhysix.physicsBody.setX(pos.x);
	    catPhysix.physicsBody.setY(pos.y);
	    catPhysix.physicsBody.setLinearVelocity(catMove.velocity, catMove.velocity);
	    manager.addComponent(entity, catPhysix);
	    manager.addComponent(entity, catMove);
	}
	
	public static void constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
		int entity = manager.createEntity();
		DogPhysicsComponent dogPhysix = new DogPhysicsComponent();
	    MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
		dogPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, dogPhysix);
	    manager.addComponent(entity, dogMove);
	}
	
	public static void constructHole(Vector2 pos){
		int entity = manager.createEntity();
		HolePhysicsComponent holePhysix = new HolePhysicsComponent();
		//PositionComponent holePosition = new PositionComponent(new Vector2((int)pos.x,(int)pos.y));
		holePhysix.initPhysics(phyManager);
	    manager.addComponent(entity, holePhysix);
	    //manager.addComponent(entity, holePosition);
	}
}
