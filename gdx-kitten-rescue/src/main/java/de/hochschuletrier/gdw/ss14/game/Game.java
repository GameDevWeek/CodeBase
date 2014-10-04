package de.hochschuletrier.gdw.ss14.game;


import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.ecs.Engine;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.systems.AnimationSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.BehaviourSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.BehaviourSystem.GlobalBlackboard;
import de.hochschuletrier.gdw.ss14.ecs.systems.CameraSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatContactSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatCooldownUpdateSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatJumpUpdateSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatMovementSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatStateUpdateSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.ChangeMapSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.CheckCatDeadSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.DeleteDeadPhysicEntitiesSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.DogMovementSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.ECSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.HitAnimationSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.InputSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.LaserPointerSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.LightMapSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.LimitedLifetimeSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.ParticleEmitterSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.PhysixUpdateSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.RenderSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.ShadowSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.TileMapRenderingSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.WoolInfluenceSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.WorldObjectsSystem;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;
import de.hochschuletrier.gdw.ss14.input.InputManager;
import de.hochschuletrier.gdw.ss14.input.InputMouse;

public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private static final float FPSLogTime = 1.0f;
    
    private Array<Float> frameTimes = new Array<Float>();

    private Array<ECSystem> systems;
    public static Engine engine;


    public static MapManager mapManager;
    private EntityManager entityManager;
    private PhysixManager physixManager;
    
    /* Behaviour */
    public static BehaviourManager behaviourManager;
    private GlobalBlackboard globalBlackboard;
    
    private Vector2 mapCenter = new Vector2();

    public Game(AssetManagerX am){
        engine = new Engine();
        entityManager = EntityManager.getInstance();
        physixManager = new PhysixManager(3.0f, 0.0f, 0.0f);
        mapManager = new MapManager(entityManager, physixManager, am);

        globalBlackboard = new GlobalBlackboard(entityManager);
        behaviourManager = new BehaviourManager(globalBlackboard);

        
        EntityFactory.phyManager = physixManager;
        EntityFactory.manager = entityManager;
        EntityFactory.assetManager = am;
        EntityFactory.behaviourManager = behaviourManager;
    }

    public void init(AssetManagerX assetManager){
        initializeSystems();
        initializeTestComponents();
        //Kartensetzen:
        mapManager.loadMap("mehrstoeckigMap"); 
        mapManager.setFloor(0);
        
        behaviourManager.activate();

    }

    private void initializeSystems()
    {
        // Game logic related systems
        engine.addSystem(new InputSystem(entityManager));
        engine.addSystem(new DeleteDeadPhysicEntitiesSystem(entityManager, physixManager));
        engine.addSystem(new ChangeMapSystem(entityManager));
        engine.addSystem(new CatMovementSystem(entityManager));
        engine.addSystem(new CatJumpUpdateSystem(entityManager));
        engine.addSystem(new CatStateUpdateSystem(entityManager));
        engine.addSystem(new CatCooldownUpdateSystem(entityManager));
        engine.addSystem(new WoolInfluenceSystem(entityManager));

       // engine.addSystem(new DogInputSystem(entityManager));
        engine.addSystem(new DogMovementSystem(entityManager));
        engine.addSystem(new HitAnimationSystem(entityManager));
        engine.addSystem(new ParticleEmitterSystem(entityManager));
        engine.addSystem(new LimitedLifetimeSystem(entityManager));
        engine.addSystem(new LaserPointerSystem(entityManager));
        engine.addSystem(new CheckCatDeadSystem(entityManager, physixManager));

        engine.addSystem(new CameraSystem(entityManager, 1024));
        engine.addSystem(new CatContactSystem(entityManager, physixManager));

        // physic systems
        engine.addSystem(new PhysixDebugRenderSystem(entityManager, physixManager));
        engine.addSystem(new PhysixUpdateSystem(entityManager, physixManager));
        engine.addSystem(new WorldObjectsSystem(entityManager));

        // Rendering related systems
        TileMapRenderingSystem map = new TileMapRenderingSystem(entityManager, 0);
        map.setLayerNameNotToRender("deco2");
        engine.addSystem(map);
        map = new TileMapRenderingSystem(entityManager, 1203);
        map.setLayerNameToRender("deco2");
        engine.addSystem(map);
        engine.addSystem(new ShadowSystem(entityManager, 9));
        engine.addSystem(new AnimationSystem(entityManager, 10));
        engine.addSystem(new RenderSystem(entityManager, 1200));
        engine.addSystem(new LightMapSystem(entityManager, 1206));
        //Behaviour System
        engine.addSystem(new BehaviourSystem(entityManager,behaviourManager ));
    }

    private void initializeTestComponents()
    {
        //int dogEntity = EntityFactory.constructDog(new Vector2(0, 0), 60.0f, 40.0f, 0, 100f);
        //int dogEntity2 = EntityFactory.constructDog(new Vector2(500, 350), 60.0f, 40.0f, 0, 100f);
        ArrayList<Vector2> patrolSpots= new ArrayList<>();
        //int dogEntity3 = EntityFactory.constructSmartDog(new Vector2(500f, 350f), 60.0f, 40.0f, 0f, 100f, patrolSpots, (short)0,(short) 0);

        EntityFactory.constructLaserPointer(new Vector2(300,0));
        EntityFactory.constructWool(new Vector2(3000,100));
        if (InputManager.getInstance().getInputDevice().getDeviceType() == DeviceType.MOUSE) {
            InputMouse mouse = (InputMouse) InputManager.getInstance().getInputDevice();
            EntityFactory.constructLaserPointer(mouse.getCursorPosition());
        } else {
            EntityFactory.constructLaserPointer(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));
        }
        
//        int dogEntity3 = EntityFactory.constructDog(new Vector2(40,200), 60.0f, 40.0f, 0, 100f);
//        int dogEntity4 = EntityFactory.constructDog(new Vector2(100, 350), 60.0f, 40.0f, 0, 100f);
//        int dogEntity5 = EntityFactory.constructDog(new Vector2(400, 200), 60.0f, 40.0f, 0, 100f);
//        int dogEntity6 = EntityFactory.constructDog(new Vector2(100, 200), 60.0f, 40.0f, 0, 100f);
    }

    public TiledMap loadMap(String filename){
        try{
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        }
        catch(Exception ex){
            throw new IllegalArgumentException("Map konnte nicht geladen werden: "+filename);
        }
    }

    public TiledMap getMap()
    {

        return null;
    }

    float timeSinceLastFPSShow = 0.0f;
    public void update(float delta){
        InputManager.getInstance().update();

        engine.update(delta);
        
        // FPS
        frameTimes.add(delta);
        
        timeSinceLastFPSShow += delta;        
        if (timeSinceLastFPSShow >= FPSLogTime) {
            showFPS();
            frameTimes.clear();
            timeSinceLastFPSShow = 0.0f;
        }

    }

    public void render(){
        engine.render();
    }
    
    public void showFPS() {
        
        float averageFrameTime = 0.0f;
        for (Float frameTime : frameTimes) {
            averageFrameTime += frameTime;
        }
        
        averageFrameTime /= frameTimes.size;
        
        String str = (int)(averageFrameTime*1000f)+" ms/Frame";
        str += " ("+(int)(1.0f/averageFrameTime)+" FPS)";
       // logger.info(str);
    }
}
