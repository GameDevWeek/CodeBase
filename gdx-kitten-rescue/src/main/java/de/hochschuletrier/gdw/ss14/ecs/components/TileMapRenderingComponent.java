package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.tiled.TiledMap;


public class TileMapRenderingComponent implements Component{
	
	public TiledMap map;
	public Array<Integer> renderedLayers = new Array<Integer>();
}
