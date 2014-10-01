package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.tiled.TiledMap;

/**
 * TiledMapRenderingComponent. Should be Component of the Class loading the Map.
 * Map Data and currently visible Layers are stored here
 * 
 * @author David Liebemann
 *
 */
public class TileMapRenderingComponent implements Component{
	
	public TiledMap map = null;
	public Array<Integer> renderedLayers = new Array<Integer>();
}
