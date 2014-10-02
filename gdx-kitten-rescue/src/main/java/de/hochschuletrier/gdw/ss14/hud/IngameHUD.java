package de.hochschuletrier.gdw.ss14.hud;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class IngameHUD {
	private CatLiveHUD catLive;
	private WaterlevelHUD waterlevel;
	
	public IngameHUD(AssetManagerX assetManager) {
		catLive = new CatLiveHUD(assetManager);
		waterlevel = new WaterlevelHUD(assetManager, catLive);
	}
    public void render() {
    	catLive.render();
    	waterlevel.render();
    }
    
    public void update() {
        
    }
}
