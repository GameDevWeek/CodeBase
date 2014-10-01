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
		List<TileMapRenderingComponent> arr = entityManager.getAllComponentsOfType(TileMapRenderingComponent.class);
		
		for(TileMapRenderingComponent currentComp : arr){
			
		    renderer = currentComp.renderer;
		    
			if (renderer == null) {
			    
    			HashMap<TileSet, Texture> tilesetImages = new HashMap();
    			
    	        for (TileSet tileset : currentComp.getMap().getTileSets()) {
    	            TmxImage img = tileset.getImage();
    	            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
    	            tilesetImages.put(tileset, new Texture(filename));
    	        }
    	        	        
    			renderer = new TiledMapRendererGdx(currentComp.getMap(), tilesetImages);
    			currentComp.renderer = renderer;
			}
	         			
			// go through the layers that should be rendered			
			for(Integer layerIndex : currentComp.renderedLayers){
				
			    if (currentComp.getMap().getLayers().size() > layerIndex) {
			        
    				Layer layerToRender = currentComp.getMap().getLayers().get(layerIndex);
    				renderer.render(0, 0, layerToRender);
			    }
			}
		}
	}
	
	public void shutdown() {
        List<TileMapRenderingComponent> arr = entityManager.getAllComponentsOfType(TileMapRenderingComponent.class);
	    
        for (TileMapRenderingComponent comp : arr) {
            comp.setMap(null);
        }
	}
	
	
	@Override
	public void update(float delta) {
		// Nothing todo
	}
}