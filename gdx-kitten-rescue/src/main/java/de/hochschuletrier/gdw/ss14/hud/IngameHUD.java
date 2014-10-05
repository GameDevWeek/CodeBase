package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class IngameHUD {
	private CatLifeHUD catLife;
	private WeaponHUD weapon;

	public IngameHUD(AssetManagerX assetManager) {
		catLife = new CatLifeHUD(assetManager);
		catLife.setScale(0.5f);


		weapon = new WeaponHUD(assetManager);
		weapon.setScale(0.8f);
	}
    public void render() {
        weapon.setPosition(Gdx.graphics.getWidth() - weapon.getWidth() - 15, 25);
        catLife.setPosition(weapon.getX() - catLife.getWidth() - 10, weapon.getY());

    	catLife.render();
    	weapon.render();
    }

    public void update() {
    }
}
