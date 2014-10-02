package de.hochschuletrier.gdw.ss14.hud;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;

public class CatLifeHUD extends HudComponent{
	
	private Texture catLiveFull;
	private Texture catLiveEmpty;
	
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
		
		EntityManager man = EntityManager.getInstance();
        int playerid = man.getAllEntitiesWithComponents(PlayerComponent.class, CatPropertyComponent.class).first();
        CatPropertyComponent cat = man.getComponent(playerid, CatPropertyComponent.class);
        
		for(int i = 0; i < CatPropertyComponent.MAX_LIVES; i++) {
			Texture tmp;
			
			if(i < CatPropertyComponent.MAX_LIVES - cat.amountLives) {
				tmp = catLiveEmpty;
			} else {
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
        return super.getWidth() * CatPropertyComponent.MAX_LIVES;
    }
}
