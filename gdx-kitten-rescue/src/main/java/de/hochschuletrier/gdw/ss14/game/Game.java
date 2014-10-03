package de.hochschuletrier.gdw.ss14.game;


import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.BehaviourSystem.GlobalBlackboard;
import de.hochschuletrier.gdw.ss14.input.InputManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game{
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private static final int FPSFrameCount = 10;
    
    private Array<Float> frameTimes = new Array<Float>();
    private int currentFrameTimeIndex = 0;

    private Array<ECSystem> systems;
    public static Engine engine;

    private MapManager mapManager;
    private EntityManager entityManager;
    private PhysixManager physixManager;
    
    /* Behaviour */
    private BehaviourManager behaviourManager;
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
        InputManager.init();
        initializeSystems();
        initializeTestComponents();

        mapManager.loadMap("Katzenklappentest");
        mapManager.setFloor(0);
        
        behaviourManager.activate();

    }

    private void initializeSystems()
    {
        // Game logic related systems
        engine.addSystem(new InputSystem(entityManager));
       // engine.addSystem(new DogInputSystem(entityManager));
        engine.addSystem(new PlayerMovementSystem(entityManager));
        engine.addSystem(new DogMovementSystem(entityManager));
        engine.addSystem(new HitAnimationSystem(entityManager));
        engine.addSystem(new ParticleEmitterSystem(entityManager));
        engine.addSystem(new LimitedLifetimeSystem(entityManager));
        engine.addSystem(new LaserPointerSystem(entityManager));
        //engine.addSystem(new ShadowSystem(entityManager));
        engine.addSystem(new CheckCatDeadSystem(entityManager, physixManager));

        engine.addSystem(new CameraSystem(entityManager, 1024));
        engine.addSystem(new CatContactSystem(entityManager, physixManager));

        // physic systems
        engine.addSystem(new PhysixDebugRenderSystem(entityManager, physixManager));
        engine.addSystem(new PhysixUpdateSystem(entityManager, physixManager));

        // Rendering related systems
        engine.addSystem(new TileMapRenderingSystem(entityManager, 0));
        engine.addSystem(new ShadowSystem(entityManager, 1));
        engine.addSystem(new AnimationSystem(entityManager, 2));
        engine.addSystem(new RenderSystem(entityManager, 1200));
        
        //Behaviour System
        engine.addSystem(new BehaviourSystem(entityManager,behaviourManager ));
    }

    private void initializeTestComponents()
    {
        //int dogEntity = EntityFactory.constructDog(new Vector2(0, 0), 60.0f, 40.0f, 0, 100f);
        //int dogEntity2 = EntityFactory.constructDog(new Vector2(500, 350), 60.0f, 40.0f, 0, 100f);
        int dogEntity3 = EntityFactory.constructSmartDog(new Vector2(500, 350), 60.0f, 40.0f, 0, 100f);

        EntityFactory.constructLaserPointer(new Vector2(300,0));
        
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
        if (frameTimes.size > currentFrameTimeIndex)
            frameTimes.set(currentFrameTimeIndex, delta);
        else
            frameTimes.add(delta);
            
        currentFrameTimeIndex = (currentFrameTimeIndex+1) % FPSFrameCount;
        
        timeSinceLastFPSShow += delta;
        
        if (timeSinceLastFPSShow >= 1.0f) {
            showFPS();
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
        logger.info(str);
    }
}
