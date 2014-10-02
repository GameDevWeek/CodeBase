package de.hochschuletrier.gdw.ss14.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;

public class CatLiveUI extends HudComponent{
	
	Texture catLiveFull;
	Texture catLiveEmpty;
	
	int maxLeben = 9;
	int aktulleLeben = 3;
	
	float scale = 0.2f;
	
	private float width;
	private float height;
	
	private float xOffset;
	private float yOffset;
	
	private float screenX;
	private float screenY;
	
	public CatLiveUI(AssetManagerX assetManager) {
		super(assetManager);
		
		catLiveFull = assetManager.getTexture("cat_live_full");
		catLiveEmpty = assetManager.getTexture("cat_live_empty");
		
		xOffset = 10;
		yOffset = 20;
		
		width = catLiveEmpty.getWidth() * scale;
		height = catLiveEmpty.getHeight() * scale;
	}
	
	public void render() {
		Main.getInstance().screenCamera.bind();
		
		for(int i = 0; i < maxLeben; i++) {
			Texture tmp;
			
			if(i < maxLeben - aktulleLeben) {
				tmp = catLiveEmpty;
			}
			else {
				tmp = catLiveFull;
			}
			
			DrawUtil.batch.draw(tmp, 
				getX() + width * i,
				getY(), 
				width,
				height,
				0, 0, tmp.getWidth(), tmp.getHeight(), false, true);
		}
	}
	
	@Override
	public float getX() {
		return Gdx.graphics.getWidth() - getWidth() - xOffset;
	}
	
	@Override
	public float getY() {
		return yOffset;
	}
	
	@Override
	public float getWidth() {
		return width * maxLeben;
	}
	
	@Override
	public float getHeigth() {
		return height;
	}

}
