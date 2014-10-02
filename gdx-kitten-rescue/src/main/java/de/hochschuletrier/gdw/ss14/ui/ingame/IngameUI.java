package de.hochschuletrier.gdw.ss14.ui.ingame;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class IngameUI {
	
	private AssetManagerX assetManager;
	
	private CatLiveUI catLiveUI;
	private WaterlevelUI waterpistolUI;
	
	public IngameUI(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		
		catLiveUI = new CatLiveUI(assetManager);
		waterpistolUI = new WaterlevelUI(assetManager);
	}
    public void render() {
    	catLiveUI.render();
    	waterpistolUI.render();
    }
    
    public void update() {
        
    }
}
