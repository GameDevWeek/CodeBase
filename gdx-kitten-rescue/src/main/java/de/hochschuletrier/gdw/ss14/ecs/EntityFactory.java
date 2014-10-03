package de.hochschuletrier.gdw.ss14.ecs;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour.DogBlackboard;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatContactSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.WorldObjectsSystem;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.physics.ICatStateListener;
import de.hochschuletrier.gdw.ss14.physics.ICollisionListener;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;
import de.hochschuletrier.gdw.ss14.states.GroundTypeState;
import de.hochschuletrier.gdw.ss14.states.JumpableState;
import de.hochschuletrier.gdw.ss14.states.ParticleEmitterTypeEnum;
import ch.qos.logback.classic.Logger;


import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.ss14.ecs.components.GroundPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.GroundPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LightComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.ShadowComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;

public class EntityFactory{

    public static int constructBalk(){
        int entity = manager.createEntity();

        return entity;
    }

    public static int constructBox(){
        int entity = manager.createEntity();

        return entity;
    }

    public static int constructBroom(){
        int entity = manager.createEntity();

        return entity;
    }

    public static int constructCat(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
        int entity = manager.createEntity();

        CatPhysicsComponent catPhysix = new CatPhysicsComponent(pos, 25, 50, 0, .2f, 0f);
        ConePhysicsComponent conePhysic = new ConePhysicsComponent(pos, 50, 1.5f, 1.57f);
        //catPhysix: position(x,y), width, height, rota, friction[0-1][ice-rubber], restitution[0-1][rock-ball]
        ICollisionListener contactSystem = (CatContactSystem) Game.engine.getSystemOfType(CatContactSystem.class);
        catPhysix.collisionListeners.add(contactSystem);

        MovementComponent catMove = new MovementComponent(maxVelocity, middleVelocity, minVelocity, acceleration);
        InputComponent catInput = new InputComponent();
        catPhysix.initPhysics(phyManager);
        conePhysic.initPhysics(phyManager);
        WeldJointPhysicsComponent jointPhysics = new WeldJointPhysicsComponent(catPhysix.physicsBody.getBody(), conePhysic.physicsBody.getBody());
        jointPhysics.initPhysics(phyManager);
        CatPropertyComponent catProperty = new CatPropertyComponent();
        catProperty.lastCheckPoint = pos;
        ICatStateListener stateSystem = (WorldObjectsSystem) Game.engine.getSystemOfType(WorldObjectsSystem.class);
        catProperty.StateListener.add(stateSystem);

        JumpDataComponent jumpDataComponent = new JumpDataComponent();

        //catPhysix.physicsBody.setLinearVelocity(catMove.velocity, catMove.velocity);
        AnimationComponent catAnimation = new AnimationComponent();

        catAnimation.animation = new AnimationExtended[32];
        catAnimation.animation[CatStateEnum.HIT.ordinal()] = assetManager.getAnimation("hit");
        catAnimation.animation[CatStateEnum.IDLE.ordinal()] = assetManager.getAnimation("idle");
        catAnimation.animation[CatStateEnum.WALK.ordinal()] = assetManager.getAnimation("walk");
        catAnimation.animation[CatStateEnum.RUN.ordinal()] = assetManager.getAnimation("run");
        catAnimation.animation[CatStateEnum.SLIDE_LEFT.ordinal()] = assetManager.getAnimation("slide_left");
        catAnimation.animation[CatStateEnum.SLIDE_RIGHT.ordinal()] = assetManager.getAnimation("slide_right");
        catAnimation.animation[CatStateEnum.CRASH.ordinal()] = assetManager.getAnimation("crash");
        catAnimation.animation[CatStateEnum.FALL.ordinal()] = assetManager.getAnimation("fall");
        catAnimation.animation[CatStateEnum.DIE.ordinal()] = assetManager.getAnimation("die");
        catAnimation.animation[CatStateEnum.DIE2.ordinal()] = assetManager.getAnimation("die2");
        catAnimation.animation[CatStateEnum.JUMP.ordinal()] = assetManager.getAnimation("jump");
//        catAnimation.animation[CatStateEnum.JUMP_BEGIN.ordinal()] = assetManager.getAnimation("jump_begin");
//        catAnimation.animation[CatStateEnum.JUMP_END.ordinal()] = assetManager.getAnimation("jump_end");

        CameraComponent cam = new CameraComponent();
        cam.cameraZoom = 1.0f;

        CatPropertyComponent catProperties = new CatPropertyComponent();
        catProperties.setState(CatStateEnum.IDLE);

        ShadowComponent shadow = new ShadowComponent();
        shadow.alpha = 0.5f;
        shadow.z = 1.0f;

        ParticleEmitterComponent particleEmitComp = new ParticleEmitterComponent();
        particleEmitComp.particleTintColor = new Color(0.5f,0.5f,0.5f,0.5f);
        particleEmitComp.emitRadius = 10f;
        particleEmitComp.emitterType = ParticleEmitterTypeEnum.PawParticleEmitter;
        particleEmitComp.particleLifetime = 20f;
        particleEmitComp.emitInterval = 0.2f;
        particleEmitComp.minimumParticleDistance = 15.0f;

        manager.addComponent(entity, jumpDataComponent);
        manager.addComponent(entity, catProperties);
        manager.addComponent(entity, catAnimation);
        manager.addComponent(entity, new RenderComponent());
        manager.addComponent(entity, catProperty);
        manager.addComponent(entity, conePhysic);
        manager.addComponent(entity, catPhysix);
        manager.addComponent(entity, catMove);
        manager.addComponent(entity, catInput);
        manager.addComponent(entity, new PlayerComponent());
        manager.addComponent(entity, cam);
        manager.addComponent(entity, shadow);
        manager.addComponent(entity, particleEmitComp);
        manager.addComponent(entity, new LightComponent());
        //manager.addComponent(entity, new ConePhysicsComponent(catPhysix.getPosition(), 100,100,100));
        //manager.addComponent(entity, new HitAnimationComponent());

        return entity;
    }

    public static int constructCatbox(Vector2 pos){
        int entity = manager.createEntity();

        CatBoxPhysicsComponent catBoxPhysicsComponent = new CatBoxPhysicsComponent(pos, 80.0f, 80.0f, 0.0f);
        catBoxPhysicsComponent.initPhysics(phyManager);
        manager.addComponent(entity, catBoxPhysicsComponent);

        return entity;
    }

    public static int constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration, ArrayList<Vector2> patrolspots){
        int entity = manager.createEntity();
        CatPhysicsComponent dogPhysix = new CatPhysicsComponent(pos, 50, 100, 0, .2f, 0f);
        ConePhysicsComponent conePhysic = new ConePhysicsComponent(pos, 50, 0, 1.5f);
        MovementComponent dogMove = new MovementComponent(maxVelocity, middleVelocity, minVelocity, acceleration);
        InputComponent dogInput = new InputComponent();
        Behaviour verhalten;
        DogPropertyComponent dogState = new DogPropertyComponent(patrolspots);
        dogPhysix.initPhysics(phyManager);
        conePhysic.initPhysics(phyManager);
        WeldJointPhysicsComponent jointPhysics = new WeldJointPhysicsComponent(dogPhysix.physicsBody.getBody(), conePhysic.physicsBody.getBody());
        jointPhysics.initPhysics(phyManager);
        
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, conePhysic);
        //manager.addComponent(entity, jointPhysics);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());
        //        manager.addComponent(entity, new AnimationComponent());      
        addDogParticleEmitter(entity);
        
        return entity;
    }
    
    private static void addDogParticleEmitter( int entity ) {
        
        ParticleEmitterComponent dogParticleEmitter = new ParticleEmitterComponent();
        dogParticleEmitter.emitInterval = 0.2f;
        dogParticleEmitter.emitRadius = 10f;
        dogParticleEmitter.particleTintColor = new Color(0.5f,0,0,0.75f);
        
        manager.addComponent(entity, dogParticleEmitter);        
    }

    public static int constructSmartDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration, ArrayList<Vector2> patrolspots){
        int entity = manager.createEntity();
        CatPhysicsComponent dogPhysix = new CatPhysicsComponent(pos, 50, 100, 0, 1,0);
        ConePhysicsComponent conePhysic = new ConePhysicsComponent(pos, 30, 0, 1.5f);
        MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
        InputComponent dogInput = new InputComponent();
        DogBehaviour.DogBlackboard localBlackboard = new DogBlackboard(manager);
        Behaviour verhalten =  new DogBehaviour("SmartDog", localBlackboard, true , entity);
        BehaviourComponent bComp = new BehaviourComponent(verhalten, behaviourManager);
        DogPropertyComponent dogState = new DogPropertyComponent(patrolspots);
        dogPhysix.initPhysics(phyManager);
        conePhysic.initPhysics(phyManager);
        WeldJointPhysicsComponent jointPhysics = new WeldJointPhysicsComponent(dogPhysix.physicsBody.getBody(), conePhysic.physicsBody.getBody());
        jointPhysics.initPhysics(phyManager);
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, conePhysic);
        //manager.addComponent(entity, jointPhysics);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());
        manager.addComponent(entity, bComp);
        addDogParticleEmitter(entity);
        
//        manager.addComponent(entity, new AnimationComponent());
        return entity;
        
    }

    public static void constructLaserPointer(Vector2 pos){
        int entity = manager.createEntity();

        LaserPointerComponent laser = new LaserPointerComponent(pos);
        
        manager.addComponent(entity, laser);
    }

    public static void constructDoor(){
        int entity = manager.createEntity();
    }

    public static void constructFood(){
        int entity = manager.createEntity();
    }

    public static int constructFloor(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef, GroundTypeState type){
        int entity = manager.createEntity();
        GroundPhysicsComponent puddlephys = new GroundPhysicsComponent(bodydef, fixturedef);
        manager.addComponent(entity, new GroundPropertyComponent(type));
        manager.addComponent(entity, puddlephys);
        puddlephys.initPhysics(phyManager);
        
        return entity;
    }
    
    public static void constructLamp(){
        int entity = manager.createEntity();
    }

    public static int constructPuddleOfBlood(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef){
        int entity = manager.createEntity();
        JumpablePhysicsComponent puddlephys = new JumpablePhysicsComponent(bodydef, fixturedef);
        manager.addComponent(entity, puddlephys);
        manager.addComponent(entity, new JumpablePropertyComponent(JumpableState.bloodpuddle));
        puddlephys.initPhysics(phyManager);
        
        
        return entity;
    }

    public static int constructPuddleOfWater(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef){
        int entity = manager.createEntity();
        JumpablePhysicsComponent puddlephys = new JumpablePhysicsComponent(bodydef, fixturedef);
        manager.addComponent(entity, puddlephys);
        manager.addComponent(entity, new JumpablePropertyComponent(JumpableState.waterpuddle));
        puddlephys.initPhysics(phyManager);
        
        
        return entity;
    }

    public static int constructDeadzone(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef){
        int entity = manager.createEntity();
        JumpablePhysicsComponent puddlephys = new JumpablePhysicsComponent(bodydef, fixturedef);
        manager.addComponent(entity, puddlephys);
        manager.addComponent(entity, new JumpablePropertyComponent(JumpableState.deadzone));
        puddlephys.initPhysics(phyManager);
        
        
        return entity;
    }
    

    public static int constructStairs(){
        int entity = manager.createEntity();
        return entity;
    }

    public static int constructVase(){
        int entity = manager.createEntity();
        return entity;
    }


    public static void constructWool(Vector2 pos){
        int entity = manager.createEntity();

        WoolPhysicsComponent woolPhysicsComponent = new WoolPhysicsComponent(pos, 20.0f, 0.0f);
        RenderComponent woolRenderComponent = new RenderComponent();
        woolPhysicsComponent.initPhysics(phyManager);
        manager.addComponent(entity, woolPhysicsComponent);

//        CatPhysicsComponent catPhysix = new CatPhysicsComponent(pos, 25, 50, 0, 0f, 0f);
//        catPhysix.initPhysics(phyManager);
//        manager.addComponent(entity, catPhysix);

        //RenderComponent renderComponent = new RenderComponent();
        //renderComponent.texture = assetManager.getTexture();
        //manager.addComponent(entity, renderComponent);
    }

    public static EntityManager manager;

    public static PhysixManager phyManager;

    public static AssetManagerX assetManager;
    
    public static BehaviourManager behaviourManager;
}

