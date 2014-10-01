package de.hochschuletrier.gdw.ss14.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.systems.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class Game
{

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static SystemComparator comparator = new SystemComparator();

    private Array<ECSystem> systems;

    private MapManager mapManager;
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private int catEntity;
    private int dogEntity;
    
    private Vector2 mapCenter = new Vector2();

    public Game(AssetManagerX am)
    {

        systems = new Array<ECSystem>();

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
        //initializeTestComponents();
        
        mapManager.loadMap("ErsteTestMap");        
        mapManager.setFloor(0);
    }

    private void initializeSystems()
    {
        // Game logic related systems
        addSystem(new InputSystem(entityManager));
        addSystem(new MovementSystem(entityManager));
        addSystem(new DogInputSystem(entityManager));
        addSystem(new PhysixDebugRenderSystem(entityManager, physixManager));
        addSystem(new PhysixUpdateSystem(entityManager, physixManager));

        addSystem(new CameraSystem(entityManager, 1024));

        // Rendering related systems
        addSystem(new TileMapRenderingSystem(entityManager, 0));
        addSystem(new AnimationSystem(entityManager, 1));
        addSystem(new RenderSystem(entityManager, 1200));
    }

    private void initializeTestComponents()
    {
        catEntity = EntityFactory.constructCat(new Vector2(500, 300), 150.0f, 75.0f, 0, 100f);
        dogEntity = EntityFactory.constructDog(new Vector2(500,350), 60.0f, 40.0f, 0, 100f);
    }

    public void addSystem(ECSystem system)
    {
        systems.add(system);
        systems.sort(comparator);
    }

    public void removeSystem(ECSystem system)
    {

        systems.removeValue(system, true);
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

        for (ECSystem system : systems) {
            system.update(delta);
        }
    }

    public void render()
    {
        for (ECSystem system : systems)
        {
            system.render();
        }
    }

    private static class SystemComparator implements Comparator<ECSystem>
    {
        @Override
        public int compare(ECSystem a, ECSystem b)
        {
            int result;

            if (a.getPriority() > b.getPriority())
            {
                result = 1;
            }
            else if (a.getPriority() == b.getPriority())
            {
                result = 0;
            }
            else
            {
                result = -1;
            }

            return result;
        }
    }
}
