package de.hochschuletrier.gdw.ss14.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.*;

import org.slf4j.Logger;
import org.slf4j.*;

import java.util.*;

public class Game
{

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static SystemComparator comparator = new SystemComparator();

    private Array<ECSystem> systems;

    private MapManager mapManager;
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private int catEntity;
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
        initializeTestComponents();
        
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

        addSystem(new CameraSystem(entityManager, 1024));

        // Rendering related systems
        addSystem(new TileMapRenderingSystem(entityManager, 0));
        addSystem(new AnimationSystem(entityManager, 1));
    }

    private void initializeTestComponents()
    {
        catEntity = EntityFactory.constructCat(mapCenter.cpy(), 10.0f, 5.0f, 0.1f, 0.1f);
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
        
        CatPhysicsComponent catPhysicsComp = entityManager.getComponent(catEntity, CatPhysicsComponent.class);
        
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            
            //testPhysics.position = testPhysics.position.add( new Vector2(100.0f, 0.0f) );
            catPhysicsComp.mPosition.add(new Vector2(10.0f, 0.0f));
        }

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
