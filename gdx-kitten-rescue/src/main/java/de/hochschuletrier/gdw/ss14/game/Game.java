package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.TileMapRenderingComponent;
import de.hochschuletrier.gdw.ss14.ecs.systems.CameraSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.ECSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.InputSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.MovementSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.TileMapRenderingSystem;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static SystemComparator comparator = new SystemComparator();
    
    private Array<ECSystem> systems;
    private EntityManager entityManager;
    
    private int catEntity;
    private Vector2 mapCenter = new Vector2();

    public Game() {
        
        entityManager = new EntityManager();
        systems = new Array<ECSystem>();
    }

    public void init(AssetManagerX assetManager) {
        
        initializeSystems();        
        initializeTestComponents();
    }
    
    private void initializeSystems() {
        
        // Game logic related systems
        addSystem( new InputSystem(entityManager) );
        addSystem( new MovementSystem(entityManager) );
        addSystem( new CameraSystem(entityManager, 1024) );
        
        // Rendering related systems
        addSystem( new TileMapRenderingSystem(entityManager, 0) );        
    }
    
    private void initializeTestComponents() {
        
        // Level entity        
        TileMapRenderingComponent newTmrComp = new TileMapRenderingComponent(); 
        newTmrComp.map = loadMap("data/maps/demo.tmx");
        newTmrComp.renderedLayers.add(0);
        newTmrComp.renderedLayers.add(1);
        
        int levelEntity = entityManager.createEntity();
        entityManager.addComponent(levelEntity, newTmrComp);

        // Cat entity        
        CameraComponent newCamComp = new CameraComponent();
        newCamComp.cameraZoom = 1.0f;
        
        PhysicsComponent newPhysicsComp = new PhysicsComponent();
        mapCenter = new Vector2(newTmrComp.map.getWidth()*newTmrComp.map.getTileWidth() * 0.5f, 
                                newTmrComp.map.getHeight()*newTmrComp.map.getTileHeight() * 0.5f);
        newPhysicsComp.dummyPosition = mapCenter.cpy();
        
        catEntity = entityManager.createEntity();
        entityManager.addComponent(catEntity, newCamComp);
        entityManager.addComponent(catEntity, newPhysicsComp);
        
    }
    
    public void addSystem(ECSystem system) {
        
        systems.add(system);
        systems.sort(comparator);
    }
    
    public void removeSystem(ECSystem system) {
        
        systems.removeValue(system, true);
    }


    public TiledMap loadMap(String filename) {
        
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }
    
    public TiledMap getMap() {
        
        return null;
    }

    public void update(float delta) {
        
        PhysicsComponent catPhysicsComp = entityManager.getComponent(catEntity, PhysicsComponent.class);
        
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            
            //testPhysics.position = testPhysics.position.add( new Vector2(100.0f, 0.0f) );
            catPhysicsComp.dummyPosition.add(new Vector2(10.0f, 0.0f));
        }
        else
            catPhysicsComp.dummyPosition.add(mapCenter.cpy().sub(catPhysicsComp.getPosition()));
        
        for (ECSystem system : systems) {
            system.update(delta);
        }
    }

    public void render() {
        
        for (ECSystem system : systems) {
            system.render();
        }
    }

    private static class SystemComparator implements Comparator<ECSystem>
    {

        @Override
        public int compare(ECSystem a, ECSystem b) {
            int result;

            if (a.getPriority() > b.getPriority()) {
                result = 1;
            }
            else if (a.getPriority() == b.getPriority()) {
                result = 0;
            }
            else {
                result = -1;
            }

            return result;
        }
    }
}
