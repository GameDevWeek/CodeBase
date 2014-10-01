package de.hochschuletrier.gdw.ss14.ecs;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.HolePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;

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
	    CatPhysicsComponent catPhysix = new CatPhysicsComponent();
	    MovementComponent catMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
	    InputComponent catInput = new InputComponent();
	    catPhysix.initPhysics(phyManager);
	    //catPhysix.physicsBody.setLinearVelocity(catMove.velocity, catMove.velocity);
	    manager.addComponent(entity, catPhysix);
	    manager.addComponent(entity, catMove);
	    manager.addComponent(entity, catInput);
	    manager.addComponent(entity, new PlayerComponent());
	}
	
	public static void constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
		int entity = manager.createEntity();
		CatPhysicsComponent dogPhysix = new CatPhysicsComponent();
	    MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
	    InputComponent dogInput = new InputComponent();
		dogPhysix.initPhysics(phyManager);
	    manager.addComponent(entity, dogPhysix);
	    manager.addComponent(entity, dogMove);
	    manager.addComponent(entity, dogInput);
	    manager.addComponent(entity, new EnemyComponent());
	}
	
	public static void constructHole(Vector2 pos){
		int entity = manager.createEntity();
		HolePhysicsComponent holePhysix = new HolePhysicsComponent();
		holePhysix.initPhysics(phyManager);
	    manager.addComponent(entity, holePhysix);
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
	
//=======
//    public static void constructCat(Vector2 pos, float maxVelocity,
//            float middleVelocity, float minVelocity, float acceleration) {
//        final int entity = manager.createEntity();
//        final CatPhysicsComponent catPhysix = new CatPhysicsComponent();
//        final PositionComponent catPosition = new PositionComponent(
//                new Vector2((int) pos.x, (int) pos.y));
//        final MovementComponent catMove = new MovementComponent(maxVelocity,
//                middleVelocity, minVelocity, acceleration, new Vector2(0, 0));
//        final CatPropertyComponent catProperty = new CatPropertyComponent();
//        catPhysix.initPhysics(phyManager);
//        manager.addComponent(entity, catProperty);
//        manager.addComponent(entity, catPhysix);
//        manager.addComponent(entity, catPosition);
//        manager.addComponent(entity, catMove);
//    }
//>>>>>>> eb05791cc2829b2ac4d6cc36dedcf0604c658cf3
//
//    public static void constructDog(Vector2 pos, float maxVelocity,
//            float middleVelocity, float minVelocity, float acceleration) {
//        final int entity = manager.createEntity();
//        final DogPhysicsComponent dogPhysix = new DogPhysicsComponent();
//        final PositionComponent dogPosition = new PositionComponent(
//                new Vector2((int) pos.x, (int) pos.y));
//        final MovementComponent dogMove = new MovementComponent(maxVelocity,
//                middleVelocity, minVelocity, acceleration, new Vector2(0, 0));
//        dogPhysix.initPhysics(phyManager);
//        manager.addComponent(entity, dogPhysix);
//        manager.addComponent(entity, dogPosition);
//        manager.addComponent(entity, dogMove);
//    }
//
//    public static void constructHole(Vector2 pos) {
//        final int entity = manager.createEntity();
//        final HolePhysicsComponent holePhysix = new HolePhysicsComponent();
//        final PositionComponent holePosition = new PositionComponent(
//                new Vector2((int) pos.x, (int) pos.y));
//        holePhysix.initPhysics(phyManager);
//        manager.addComponent(entity, holePhysix);
//        manager.addComponent(entity, holePosition);
//    }
//
//    public static EntityManager manager;
//
//    public static PhysixManager phyManager;
//
//    public EntityFactory() {
//
//    }
//
//    // public static void constructDog(Vector2 pos, float maxVelocity, float
//    // middleVelocity, float minVelocity, float acceleration){
//    // int entity = manager.createEntity();
//    // DogPhysicsComponent dogPhysix = new DogPhysicsComponent();
//    // PositionComponent dogPosition = new PositionComponent(new
//    // Vector2((int)pos.x,(int)pos.y));
//    // MovementComponent dogMove = new
//    // MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration,new
//    // Vector2(0,0));
//    // dogPhysix.initPhysics(phyManager);
//    // manager.addComponent(entity, dogPhysix);
//    // manager.addComponent(entity, dogPosition);
//    // manager.addComponent(entity, dogMove);
//    // }
}