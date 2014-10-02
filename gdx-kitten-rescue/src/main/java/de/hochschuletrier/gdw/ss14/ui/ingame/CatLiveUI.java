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
	Texture catLiveEmpty;
	
	int maxLeben = 9;
	int aktulleLeben = 3;
	
	float scale = 0.2f;
	
	public CatLiveUI(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		
		catLiveFull = assetManager.getTexture("cat_live_full");
		catLiveEmpty = assetManager.getTexture("cat_live_empty");
	}
	
	public void render() {
		Main.getInstance().screenCamera.bind();
        
		for( int i = 1; i <= aktulleLeben; i++) {
			DrawUtil.batch.draw(catLiveFull, 
					Gdx.graphics.getWidth() - ((catLiveFull.getWidth() * scale) * i),
					20, 
					catLiveFull.getWidth() * scale,
					catLiveFull.getHeight() * scale,
					0, 0, catLiveFull.getWidth(), catLiveFull.getHeight(), false, true);
		}
		for( int i = aktulleLeben + 1; i <= maxLeben; i++) {
			DrawUtil.batch.draw(catLiveEmpty, 
					Gdx.graphics.getWidth() - ((catLiveEmpty.getWidth() * scale) * i),
					20, 
					catLiveEmpty.getWidth()* scale,
					catLiveEmpty.getHeight() *scale,
					0, 0, catLiveEmpty.getWidth(), catLiveEmpty.getHeight(), false, true);
		}
	}

}
