package de.hochschuletrier.gdw.ss14.ecs.systems;

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
import de.hochschuletrier.gdw.ss14.ecs.components.TileMapRenderingComponent;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.systems.ECSystem;

/**
 * Map Rendering System, using TiledMapRendererGdx by Santo Pfingsten
 * 
 * @author David Liebemann
 *
 */
public class TileMapRenderingSystem extends ECSystem{
	
	/**
	 * renderer, doing background drawing stuff
	 */
	private TiledMapRendererGdx renderer;
	
    public TileMapRenderingSystem(EntityManager entityManager){
        super(entityManager);
    }
    
    public TileMapRenderingSystem(EntityManager entityManager, int priority){
        super(entityManager, priority);
    }
    
	
	/**
	 * Render all Layers in all TileMapRenderingComponents
	 * 
	 * Meaning: Every Level will have TileMapRenderingComponents. 
	 * They contain the map data and the Layers that should be drawn at the moment.
	 * These will be rendered.
	 */
	public void render(){
		// Get all TileMapRenderingComponents. Most likely will be only one...
		List<TileMapRenderingComponent> arr =  entityManager.getAllComponentsOfType(TileMapRenderingComponent.class);
		
		for(int i = 0; i < arr.size(); i++){
			
			/*
			 * This could be a huge performance impact. Because we want to - 
			 * theoretically be able to have multiple maps loaded (even though we will
			 * most likely only have one at all time), we can't preload and save
			 * the renderer as is usually done.
			 */
			HashMap<TileSet, Texture> tilesetImages = new HashMap();
	        for (TileSet tileset : arr.get(i).map.getTileSets()) {
	            TmxImage img = tileset.getImage();
	            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
	            tilesetImages.put(tileset, new Texture(filename));
	        }
	        
	        // here it is. all these renderers. argh...
			renderer = new TiledMapRendererGdx(arr.get(i).map, tilesetImages);
			
			// Get the current component
			TileMapRenderingComponent t = arr.get(i);
			
			// go through the layers that should be rendered			
			for(Integer layerIndex : t.renderedLayers){
				
			    if (t.map.getLayers().size() > layerIndex) {
			        
    				Layer layerToRender = t.map.getLayers().get(layerIndex);
    				renderer.render(0, 0, layerToRender);
			    }
			}
			
		}
	}
	
	
	
	@Override
	public void update(float delta) {
		// Nothing todo
	}
}