package de.hochschuletrier.gdw.ss14.ui.ingame;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public abstract class HudComponent {
	
	protected AssetManagerX assetManager;
	protected HudComponent parent;

	public abstract void render();
	
	public HudComponent(AssetManagerX assetManager) {
		this.assetManager = assetManager;
	}
	
	public HudComponent(AssetManagerX assetManager, HudComponent parent) {
        this.assetManager = assetManager;
        this.parent = parent;
    }
	
	public HudComponent getParent() {
	    return this.parent;
	}
	
	public abstract float getX();
	
	public abstract float getY();
	
	public abstract float getWidth();
	
	public abstract float getHeigth();
}
