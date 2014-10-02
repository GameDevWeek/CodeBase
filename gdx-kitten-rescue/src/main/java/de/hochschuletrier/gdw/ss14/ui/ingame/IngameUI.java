package de.hochschuletrier.gdw.ss14.ui.ingame;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class IngameUI {
	
	private AssetManagerX assetManager;
	
	private CatLiveUI catLiveUI;
	
	public IngameUI(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		
		catLiveUI = new CatLiveUI(assetManager);
	}
    public void render() {
    	catLiveUI.render();
    }
    
    public void update() {
        
    }
}
