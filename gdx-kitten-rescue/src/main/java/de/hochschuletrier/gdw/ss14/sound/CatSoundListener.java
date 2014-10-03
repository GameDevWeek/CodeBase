package de.hochschuletrier.gdw.ss14.sound;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.physics.ICatStateListener;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

public class CatSoundListener implements ICatStateListener {
	private Sound walkSound;
	private Sound loop;
	private boolean isLooping;
	private CatPropertyComponent player;
	private AssetManagerX assetManager;
	private static Logger Logger = LoggerFactory.getLogger(SoundManager.class);
	private static float SystemVolume = 1.9f;
	
	public CatSoundListener(AssetManagerX assetManager) {
		this.assetManager = assetManager;
		this.player = this.getPlayer();
		this.register();
	}
	
	public void register() {
		this.player.StateListener.add(this);
	}
	
	private CatPropertyComponent getPlayer() {
		Array<Integer> allEntities = new Array<Integer>();
		allEntities = (EntityManager.getInstance().getAllEntitiesWithComponents(CatPropertyComponent.class));
		int playerEntityID = allEntities.first();
		CatPropertyComponent playerProperties = EntityManager.getInstance().getComponent(playerEntityID, CatPropertyComponent.class);
		
		return playerProperties;
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
		this.walkSound = this.assetManager.getSound(sound);
		this.walkSound.loop(CatSoundListener.SystemVolume * LocalMusic.getSystemVolume());
	}

	@Override
	public void stateChanged(CatStateEnum oldstate, CatStateEnum newstate) {
		this.processSound(newstate);
	}
}
