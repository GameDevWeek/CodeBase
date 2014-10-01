package de.hochschuletrier.gdw.ss14.sandbox.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.ecs.Engine;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.systems.DogInputSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.InputSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.MovementSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.PhysixRenderSystem;
import de.hochschuletrier.gdw.ss14.ecs.systems.PhysixUpdateSystem;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;


public class Spielwelt extends SandboxGame{
	
	private static final Logger logger = LoggerFactory.getLogger(Spielwelt.class);
	private Engine engine;
	private EntityManager manager;
	private PhysixManager phyManager;

	@Override
	public void init(AssetManagerX assetManager) {
		engine = new Engine();
		manager = new EntityManager();
		phyManager = new PhysixManager(3,0,0);
		EntityFactory ef = new EntityFactory();
		int entity = manager.createEntity();
		EntityFactory.constructCat(new Vector2(200,200), 10, 5, 3, 1.2f);
		EntityFactory.constructDog(new Vector2(0,0), 10, 5, 3, 1.2f);
		engine.addSystem(new MovementSystem(manager));
		engine.addSystem(new PhysixRenderSystem(manager,phyManager));
		engine.addSystem(new InputSystem(manager));
		engine.addSystem(new PhysixUpdateSystem(manager,phyManager));
		engine.addSystem(new DogInputSystem(manager));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		engine.render();
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		engine.update(delta);
	}
	
	public TiledMap loadMap(String file){
		try{
			return new TiledMap(file, LayerObject.PolyMode.ABSOLUTE);
		}catch(Exception e){
			 throw new IllegalArgumentException("Map konnte nicht geladen werden: " + file);
		}
	}
	

}
