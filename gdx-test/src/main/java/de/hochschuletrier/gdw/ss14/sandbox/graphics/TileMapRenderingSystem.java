package de.hochschuletrier.gdw.ss14.sandbox.graphics;

import java.util.HashMap;
import java.util.List;





import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;
import de.hochschuletrier.gdw.ss14.sandbox.testDavidLiebemann.*;

public class TileMapRenderingSystem extends ECSystem{
	
	private TiledMapRendererGdx renderer;
	
	public TileMapRenderingSystem(EntityManager entityManager){
		super(entityManager);
	}
	
	
	/**
	 * Render all Layers in all Components
	 */
	public void render(){
		List<TileMapRenderingComponent> arr =  entityManager.getAllComponentsOfType(TileMapRenderingComponent.class);
		
		for(int i = 0; i < arr.size(); i++){
			
			HashMap<TileSet, Texture> tilesetImages = new HashMap();
	        for (TileSet tileset : arr.get(i).map.getTileSets()) {
	            TmxImage img = tileset.getImage();
	            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
	            tilesetImages.put(tileset, new Texture(filename));
	        }
	        
			renderer = new TiledMapRendererGdx(arr.get(i).map, tilesetImages);
			
			TileMapRenderingComponent t = arr.get(i);
			
			for(Integer layerIndex : t.renderedLayers){
				
				Layer layerToRender = t.map.getLayers().get(layerIndex);
				renderer.render(0, 0, layerToRender);
			}
			
		}
	}
	
	
	
	@Override
	public void update(float delta) {
		// Nothing todo
	}
}