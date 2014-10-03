package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class IngameHUD {
	private CatLifeHUD catLife;
	private WaterlevelHUD waterlevel;
	private WeaponHUD weapon;
	
	public IngameHUD(AssetManagerX assetManager) {
		catLife = new CatLifeHUD(assetManager);
		catLife.setScale(0.2f);
		
		waterlevel = new WaterlevelHUD(assetManager, 25f, catLife.getWidth());
		
		weapon = new WeaponHUD(assetManager);
		weapon.setScale(0.3f);
	}
    public void render() {
        weapon.setPosition(Gdx.graphics.getWidth() - weapon.getWidth() - 15, 25);
        catLife.setPosition(weapon.getX() - catLife.getWidth() - 10, weapon.getY());
        waterlevel.setPosition(catLife.getX(), catLife.getY() + catLife.getHeight());
        
    	catLife.render();
    	waterlevel.render();
    	weapon.render();
    }
    
    public void update() {
    }
}
