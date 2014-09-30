package de.hochschuletrier.gdw.ss14.sandbox.Test;

import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Entity.EntityFactory;
import de.hochschuletrier.gdw.ss14.sandbox.Test.System.MovementSystem;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.Engine;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spielwelt extends SandboxGame{
	
	private static final Logger logger = LoggerFactory.getLogger(Spielwelt.class);
	private Engine engine;
	private EntityManager manager;

	@Override
	public void init(AssetManagerX assetManager) {
        //Don't push broken stuff ! @DerJenigeWelche
		//EntityFactory ef = new EntityFactory(manager, new PhysixManager(0, 0, 0));
		engine = new Engine();
		manager = new EntityManager();
		int entity = manager.createEntity();
		EntityFactory.constructCat(new Vector2(0,0), 10, 5, 3, 1.2f);
		engine.addSystem(new MovementSystem(manager));
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		engine.update(0.2f);
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	public TiledMap loadMap(String file){
		try{
			return new TiledMap(file, LayerObject.PolyMode.ABSOLUTE);
		}catch(Exception e){
			 throw new IllegalArgumentException("Map konnte nicht geladen werden: " + file);
		}
	}
	

}
