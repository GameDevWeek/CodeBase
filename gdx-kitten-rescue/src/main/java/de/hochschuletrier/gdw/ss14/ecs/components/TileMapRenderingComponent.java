package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;

/**
 * TiledMapRenderingComponent. Should be Component of the Class loading the Map.
 * Map Data and currently visible Layers are stored here
 * 
 * @author David Liebemann
 *
 */
public class TileMapRenderingComponent implements Component{
	
    public static float LayerSwitchTime = 4.0f;

    private TiledMap map = null;
	public Array<Integer> renderedLayers = new Array<Integer>();
	
	// Used to switch floors
	private Array<Integer> nextRenderedLayers = null;
	public float currentSwitchTime = 0.0f;
	
	public TiledMapRendererGdx renderer = null;
	
	public Array<Integer> getNextRenderedLayers() {
	    
	    return nextRenderedLayers;
	}
	
	public void fadeToRenderedLayers( Array<Integer> nrl ) {
	    
	    nextRenderedLayers = nrl;
	    currentSwitchTime = 0.0f;
	}
	
	public void setMap( TiledMap newMap ) {
	    
	    if (renderer != null) {	        
	        renderer.dispose();
	        renderer = null;
	    }
	    
	    map = newMap;
	}
	
	public TiledMap getMap() {
	    return map;
	}
}
