package de.hochschuletrier.gdw.ss14.sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

public class CatSoundListener {
	private Sound walkSound;
	private Sound loop;
	private boolean isLooping;
	private static Logger Logger = LoggerFactory.getLogger(SoundManager.class);
	private static AssetManagerX assetManager;
	private static float SystemVolume = 1.9f;
	
	public CatSoundListener(AssetManagerX assetManager) {
		this.assetManager = assetManager;
	}
	
	public void updateCat() {
		Array<Integer> allEntities = new Array<Integer>();
		allEntities = (EntityManager.getInstance().getAllEntitiesWithComponents(CatPropertyComponent.class));
		int playerEntityID = allEntities.first();
		CatPropertyComponent playerProperties = EntityManager.getInstance().getComponent(playerEntityID, CatPropertyComponent.class);
		this.processSound(playerProperties.getState());
	}
	
	private void processSound(CatStateEnum catState) {
		switch(catState) {
			case WALK:
			case RUN:
				this.walkSound("gp_cat_walk_laminate");
				break;
			default:
				if (this.walkSound == null)
					break;
				this.walkSound.stop();
				this.isLooping = false;
				break;
		}
	}
	
	private void walkSound(String sound) {
		if (this.isLooping)
			return;
		
		this.isLooping = true;
		this.walkSound = CatSoundListener.assetManager.getSound(sound);
		this.walkSound.loop(CatSoundListener.SystemVolume * LocalMusic.getSystemVolume());
	}
}
