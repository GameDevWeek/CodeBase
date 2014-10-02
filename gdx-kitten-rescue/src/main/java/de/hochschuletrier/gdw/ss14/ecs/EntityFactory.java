package de.hochschuletrier.gdw.ss14.ecs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationWithVariableFrameTime;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour.DogBlackboard;
import de.hochschuletrier.gdw.ss14.ecs.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.BehaviourComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.HitAnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;


public class EntityFactory {

    public static void constructBalk() {
        int entity = manager.createEntity();
    }

    public static void constructBox() {
        int entity = manager.createEntity();
    }

    public static void constructBroom() {
        int entity = manager.createEntity();
    }

    public static int constructCat(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration) {
        
        int entity = manager.createEntity();
        
        CatPhysicsComponent catPhysix = new CatPhysicsComponent(pos, 50, 100, 0, 1, 0);
        MovementComponent catMove = new MovementComponent(maxVelocity, middleVelocity, minVelocity, acceleration);
        InputComponent catInput = new InputComponent();
        catPhysix.initPhysics(phyManager);
        CatPropertyComponent catProperty = new CatPropertyComponent();
        //catPhysix.physicsBody.setLinearVelocity(catMove.velocity, catMove.velocity);
        AnimationComponent catAnimation = new AnimationComponent();
        
        catAnimation.animation = new AnimationWithVariableFrameTime[6];
        catAnimation.animation[CatStateEnum.HIT.ordinal()]
                = loadAnimation("data/animations/Hit_rdy.png", 5, 1, new float[]{0.1f, 0.5f, 0.1f, 0.1f, 0.1f}, Animation.PlayMode.NORMAL);
        catAnimation.animation[CatStateEnum.IDLE.ordinal()]
                = loadAnimation("data/animations/Schwanz_rdy.png", 10, 1, 0.2f, Animation.PlayMode.LOOP);
        catAnimation.animation[CatStateEnum.WALK.ordinal()]
                = loadAnimation("data/animations/Laufen_rdy.png", 4, 1, new float[]{0.1f, 0.2f, 0.1f, 0.2f}, Animation.PlayMode.LOOP);
        catAnimation.animation[CatStateEnum.RUN.ordinal()]
                = loadAnimation("data/animations/Rennen_rdy.png", 4, 1, new float[]{0.1f, 0.2f, 0.1f, 0.2f}, Animation.PlayMode.LOOP);
        catAnimation.animation[CatStateEnum.SLIDE_LEFT.ordinal()]
                = loadAnimation("data/animations/Rutschen_links_rdy.png", 5, 1, new float[]{0.1f, 0.2f, 0.5f, 0.1f, 0.1f}, Animation.PlayMode.NORMAL);
        catAnimation.animation[CatStateEnum.SLIDE_RIGHT.ordinal()]
                = loadAnimation("data/animations/Rutschen_rechts_rdy.png", 5, 1, new float[]{0.1f, 0.2f, 0.5f, 0.1f, 0.1f}, Animation.PlayMode.NORMAL);

        CameraComponent cam = new CameraComponent();
        cam.cameraZoom = 1.0f;

        CatPropertyComponent catProperties = new CatPropertyComponent();
        catProperties.state = CatStateEnum.IDLE;

        manager.addComponent(entity, catProperties);
        manager.addComponent(entity, catAnimation);
        manager.addComponent(entity, new RenderComponent());
        manager.addComponent(entity, catProperty);
        manager.addComponent(entity, catPhysix);
        manager.addComponent(entity, catMove);
        manager.addComponent(entity, catInput);
        manager.addComponent(entity, new PlayerComponent());
        manager.addComponent(entity, cam);
        //manager.addComponent(entity, new HitAnimationComponent());

        return entity;
    }

    public static void constructCatbox() {
        int entity = manager.createEntity();
    }

    public static int constructDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration) {
        int entity = manager.createEntity();
        CatPhysicsComponent dogPhysix = new CatPhysicsComponent(pos, 50, 100, 0, 1,0);
        MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
        InputComponent dogInput = new InputComponent();
        Behaviour verhalten;
        DogPropertyComponent dogState = new DogPropertyComponent();
        dogPhysix.initPhysics(phyManager);
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());
//        manager.addComponent(entity, new AnimationComponent());
        return entity;
    }

    public static int constructSmartDog(Vector2 pos, float maxVelocity, float middleVelocity, float minVelocity, float acceleration){
        int entity = manager.createEntity();
        CatPhysicsComponent dogPhysix = new CatPhysicsComponent(pos, 50, 100, 0, 1,0);
        MovementComponent dogMove = new MovementComponent(maxVelocity,middleVelocity,minVelocity,acceleration);
        InputComponent dogInput = new InputComponent();
        DogBehaviour.DogBlackboard localBlackboard = new DogBlackboard(manager);
        Behaviour verhalten =  new DogBehaviour("SmartDog", localBlackboard, true , entity);
        BehaviourComponent bComp = new BehaviourComponent(verhalten, behaviourManager);
        DogPropertyComponent dogState = new DogPropertyComponent();
        dogPhysix.initPhysics(phyManager);
        manager.addComponent(entity, dogState);
        manager.addComponent(entity, dogPhysix);
        manager.addComponent(entity, dogMove);
        manager.addComponent(entity, dogInput);
        manager.addComponent(entity, new EnemyComponent());
        manager.addComponent(entity, bComp);

//        manager.addComponent(entity, new AnimationComponent());
        return entity;
        
    }
    
    public static void constructDoor() {
        int entity = manager.createEntity();
    }

    public static void constructFood() {
        int entity = manager.createEntity();
    }

    public static void constructLamp() {
        int entity = manager.createEntity();
    }

    public static void constructPuddleOfBlood() {
        int entity = manager.createEntity();
    }

    public static void constructPuddleOfWater() {
        int entity = manager.createEntity();
    }

    public static void constructStairs() {
        int entity = manager.createEntity();
    }

    public static void constructVase() {
        int entity = manager.createEntity();
    }

    public static void constructWool() {
        int entity = manager.createEntity();
    }

    private static AnimationWithVariableFrameTime loadAnimation(String path, int cols, int row, float frameDuration, Animation.PlayMode playMode) {
        Texture tex;
        TextureRegion[][] tmp;
        TextureRegion[] frames;

        tex = new Texture(path);
        tmp = TextureRegion.split(tex, tex.getWidth() / cols, tex.getHeight() / row);
        frames = new TextureRegion[cols * row];
        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        AnimationWithVariableFrameTime ani = new AnimationWithVariableFrameTime(frameDuration, frames);
        ani.setPlayMode(playMode);
        return ani;
    }

    private static AnimationWithVariableFrameTime loadAnimation(String path, int cols, int row, float frameDurations[], Animation.PlayMode playMode) {
        AnimationWithVariableFrameTime ani = loadAnimation(path, cols, row, 0, playMode);
        ani.setFrameDurations(frameDurations);
        return ani;
    }

    public static EntityManager manager;

    public static PhysixManager phyManager;

    public static AssetManagerX assetManager;
    
    public static BehaviourManager behaviourManager;
}

