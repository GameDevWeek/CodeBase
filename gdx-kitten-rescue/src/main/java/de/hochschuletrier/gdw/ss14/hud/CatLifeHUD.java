package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;

public class CatLifeHUD extends HudComponent{
	
	Texture catLiveFull;
	Texture catLiveEmpty;
	
	int maxLeben = 9;
	int aktulleLeben = 3;
	
	public CatLifeHUD(AssetManagerX assetManager) {
		super(assetManager);
		
		catLiveFull = assetManager.getTexture("cat_live_full");
		catLiveEmpty = assetManager.getTexture("cat_live_empty");
		
		width = catLiveEmpty.getWidth();
        height = catLiveEmpty.getHeight();
	}
	
	@Override
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
				getX() + super.getWidth() * i,
				getY(), 
				super.getWidth(),
				super.getHeight(),
				0, 0, tmp.getWidth(), tmp.getHeight(), false, true);
		}
	}

    @Override
    public float getWidth() {
        return super.getWidth() * maxLeben;
    }
}
