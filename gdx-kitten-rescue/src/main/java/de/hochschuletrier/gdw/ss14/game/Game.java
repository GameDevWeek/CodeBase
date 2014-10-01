package de.hochschuletrier.gdw.ss14.game;


import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.*;
import org.slf4j.Logger;
import org.slf4j.*;

public class Game
{
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private Array<ECSystem> systems;
    private Engine engine;

    private MapManager mapManager;
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private int catEntity;

    private Vector2 mapCenter = new Vector2();

    public Game(AssetManagerX am)
    {
        engine = new Engine();
        entityManager = new EntityManager();
        physixManager = new PhysixManager(3.0f, 0.0f, 0.0f);
        mapManager = new MapManager(entityManager, physixManager, am);

        EntityFactory.phyManager = physixManager;
        EntityFactory.manager = entityManager;
        EntityFactory.assetManager = am;
    }

    public void init(AssetManagerX assetManager)
    {
        initializeSystems();
        initializeTestComponents();

        mapManager.loadMap("ErsteTestMap");
        mapManager.setFloor(0);
    }

    private void initializeSystems()
    {
        // Game logic related systems
        engine.addSystem(new InputSystem(entityManager));
        engine.addSystem(new MovementSystem(entityManager));
        engine.addSystem(new DogInputSystem(entityManager));
        engine.addSystem(new PhysixDebugRenderSystem(entityManager, physixManager));
        engine.addSystem(new PhysixUpdateSystem(entityManager, physixManager));
        engine.addSystem(new HitAnimationSystem(entityManager));

        engine.addSystem(new CameraSystem(entityManager, 1024));

        // Rendering related systems
        engine.addSystem(new TileMapRenderingSystem(entityManager, 0));
        engine.addSystem(new AnimationSystem(entityManager, 1));
        engine.addSystem(new RenderSystem(entityManager, 1200));
    }

    private void initializeTestComponents()
    {
        int dogEntity = EntityFactory.constructDog(new Vector2(0, 0), 60.0f, 40.0f, 0, 100f);
        int dogEntity2 = EntityFactory.constructDog(new Vector2(500, 350), 60.0f, 40.0f, 0, 100f);
    }

    public TiledMap loadMap(String filename)
    {
        try
        {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex)
        {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    public TiledMap getMap()
    {

        return null;
    }

    public void update(float delta)
    {
        /*CatPhysicsComponent catPhysicsComp = entityManager.getComponent(catEntity, CatPhysicsComponent.class);
        
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            
            //testPhysics.position = testPhysics.position.add( new Vector2(100.0f, 0.0f) );
//            catPhysicsComp.dummyPosition.add(new Vector2(10.0f, 0.0f));
        }
        else{
//            catPhysicsComp.dummyPosition.add(mapCenter.cpy().sub(catPhysicsComp.getPosition()));
            catPhysicsComp.dummyPosition.add(mapCenter.cpy().sub(catPhysicsComp.getPosition()));
        
            catPhysicsComp.mPosition.add(new Vector2(10.0f, 0.0f));
        }*/

        engine.update(delta);
    }

    public void render()
    {
        engine.render();
    }
}
