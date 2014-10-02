package de.hochschuletrier.gdw.ss14.ui.ingame;

import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public abstract class HudComponent {
	
	protected AssetManagerX assetManager;

	public abstract void render();
	
	public HudComponent(AssetManagerX assetManager) {
		this.assetManager = assetManager;
	}
	
	public abstract float getX();
	
	public abstract float getY();
	
	public abstract float getWidth();
	
	public abstract float getHeigth();
}
