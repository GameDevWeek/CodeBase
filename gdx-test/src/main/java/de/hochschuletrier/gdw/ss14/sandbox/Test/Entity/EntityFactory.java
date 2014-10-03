package de.hochschuletrier.gdw.ss14.sandbox.Test.Entity;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.CatStateComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.DogStateComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.EnemyComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.HolePhysicsComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.InputComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.MovementComponent;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Component.PlayerComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;


public class EntityFactory {

    public static void constructBalk(){
        int entity = manager.createEntity();
    }
    public static void constructBox(){
        int entity = manager.createEntity();
    }
    public static void constructBroom(){
        int entity = manager.createEntity();
    }

    public static void constructCat(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
        int entity = manager.createEntity();
        CatPhysicsComponent catPhysix = new CatPhysicsComponent(pos, 50, 100, 0, 1,0);
        MovementComponent catMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
        InputComponent catInput = new InputComponent();
        catPhysix.initPhysics(phyManager);
        CatStateComponent catState = new CatStateComponent();
        //catPhysix.physicsBody.setLinearVelocity(catMove.velocity, catMove.velocity);
        manager.addComponent(entity, catState);
        manager.addComponent(entity, catPhysix);
        manager.addComponent(entity, catMove);
        manager.addComponent(entity, catInput);
        manager.addComponent(entity, new PlayerComponent());
    }

    public static void constructCatbox(){
        int entity = manager.createEntity();
    }

    public static void constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
        int entity = manager.createEntity();
        CatPhysicsComponent dogPhysix = new CatPhysicsComponent();
        MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
        InputComponent dogInput = new InputComponent();
        DogStateComponent dogState = new DogStateComponent();
        dogPhysix.initPhysics(phyManager);
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());
    }


    public static void constructDoor(){
        int entity = manager.createEntity();
    }

    public static void constructFood(){
        int entity = manager.createEntity();
    }

    public static void constructHole(Vector2 pos){
        int entity = manager.createEntity();
        HolePhysicsComponent holePhysix = new HolePhysicsComponent();
        holePhysix.initPhysics(phyManager);
        manager.addComponent(entity, holePhysix);
    }


    public static void constructLamp(){
        int entity = manager.createEntity();
    }

    public static void constructPuddleOfBlood(){
        int entity = manager.createEntity();
    }

    public static void constructPuddleOfWater(){
        int entity = manager.createEntity();
    }

    public static void constructStairs(){
        int entity = manager.createEntity();
    }

    public static void constructVase(){
        int entity = manager.createEntity();
    }

    public static void constructWool(){
        int entity = manager.createEntity();
    }


    public static EntityManager manager;

    public static PhysixManager phyManager;

    public static AssetManagerX assetManager;

    public EntityFactory(EntityManager manager, PhysixManager phyManager, AssetManagerX assetManager){
        this.manager = manager;
        this.phyManager = phyManager;
        this.assetManager = assetManager;
    }

}