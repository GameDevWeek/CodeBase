package de.hochschuletrier.gdw.ss14.sandbox.graphics;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;


public class TileMapRenderingComponent implements Component{
	
	public TiledMap map;
	public Array<Integer> renderedLayers = new Array<Integer>();
}
