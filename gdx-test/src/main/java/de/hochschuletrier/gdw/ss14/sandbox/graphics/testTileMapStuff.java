package de.hochschuletrier.gdw.ss14.sandbox.graphics;

import java.util.HashMap;

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;

public class testTileMapStuff extends SandboxGame{
	
	EntityManager emng;
	TileMapRenderingComponent cmp;
	TileMapRenderingSystem sys;
	TiledMap map;
	int test;

	@Override
	public void init(AssetManagerX assetManager) {
		emng = new EntityManager();
		cmp = new TileMapRenderingComponent();
		sys = new TileMapRenderingSystem(emng);
		
		map = loadMap("data/maps/demo.tmx");
        
        
        cmp.map = map;

        cmp.renderedLayers.add(0);
		
		test = emng.createEntity();
		
		emng.addComponent(test, cmp);
		
	}
	
	public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		sys.render();
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}
