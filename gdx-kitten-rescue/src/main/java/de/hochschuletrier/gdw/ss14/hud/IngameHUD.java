package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public class IngameHUD {
	private CatLifeHUD catLife;
	private WaterlevelHUD waterlevel;
	
	public IngameHUD(AssetManagerX assetManager) {
		catLife = new CatLifeHUD(assetManager);
		catLife.setScale(0.2f);
		
		waterlevel = new WaterlevelHUD(assetManager, 25f, catLife.getWidth());
	}
    public void render() {
        catLife.setPosition(Gdx.graphics.getWidth() - catLife.getWidth() - 10, 20);
        waterlevel.setPosition(catLife.getX(), catLife.getY() + catLife.getHeigth());
        
    	catLife.render();
    	waterlevel.render();
    }
    
    public void update() {
        /*
        EntityManager man = new EntityManager();
        int playerid = man.getAllEntitiesWithComponents(PlayerComponent.class, CatPropertyComponent.class).first();
        CatPropertyComponent cat = man.getComponent(playerid, CatPropertyComponent.class);
        cat.amountLives
        */
    }
}
