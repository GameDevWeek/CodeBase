package Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;

public class Spielwelt extends SandboxGame{
	
	private static final Logger logger = LoggerFactory.getLogger(Spielwelt.class);

	@Override
	public void init(AssetManagerX assetManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
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
