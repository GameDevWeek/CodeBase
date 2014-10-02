package de.hochschuletrier.gdw.ss14.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;

public class CatLiveUI {
	
private AssetManagerX assetManager;
	
	Texture catLiveFull;
	
	public CatLiveUI(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		
		catLiveFull = assetManager.getTexture("cat_live_full");
	}
	
	public void render() {
		Main.getInstance().screenCamera.bind();
        
        DrawUtil.batch.draw(catLiveFull, 0, 0, catLiveFull.getWidth(), catLiveFull.getHeight(), 0, 0,
        		catLiveFull.getWidth(), catLiveFull.getHeight(), false, true);
        /*
        DrawUtil.batch.draw(walking.getKeyFrame(stateTime), x,
                512, walking.getKeyFrame(0f).getRegionWidth(), -walking.getKeyFrame(0f).getRegionHeight());
                */
	}

}
