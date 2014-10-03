package de.hochschuletrier.gdw.ss14.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.*;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;
import de.hochschuletrier.gdw.ss14.states.GameStates;
import de.hochschuletrier.gdw.ss14.ui.UIActions;

public class SoundManager {
	private static Sound sound;
	private static Sound loop;
	private static boolean isLooping;
	private static AssetManagerX assetManager;
	private static float SystemVolume = 1.9f;
	
	public static void performAction(Enum action) {
		GameStates actualGamestate = null;
		String actionString = action.name();

		for (GameStates state : GameStates.values()) {
			if (state.isActive()) {
				actualGamestate = state;
				break;
			}
		}
		
		switch(actualGamestate) {
			case MAINMENU:
			case OPTIONSMENU:
			case PAUSEGAME:
				switch(actionString) {
					case "BELLOVER":
						SoundManager.playSound("ui_bell"+SoundManager.random(1, 5));
						break;
					case "BELLCLICKED":
						SoundManager.playSound("ui_menu");
				}
				break;
			case GAMEPLAY:
				switch (actionString) {
					case "CATWALK":
						//System.out.println("WALKING CAT!!!");
						Array<Integer> entities = new Array<Integer>();
						entities = (EntityManager.getInstance().getAllEntitiesWithComponents(CatPropertyComponent.class));

						int playerEntityID = entities.first();
						CatPropertyComponent playerProperties = EntityManager.getInstance().getComponent(playerEntityID, CatPropertyComponent.class);
						if (playerProperties.getState() != CatStateEnum.WALK && playerProperties.getState() != CatStateEnum.RUN) {
							SoundManager.loop.stop();
							SoundManager.isLooping = false;
						}
						else 
							SoundManager.loopSound("gp_cat_walk_laminate");
						break;
				} 
				break;
			default:
				break;
				
		}
	}

	private static void playSound(String sound) {
		SoundManager.sound = SoundManager.assetManager.getSound(sound);
		SoundManager.sound.play(SoundManager.SystemVolume * LocalMusic.getSystemVolume());
	}
	
	private static void loopSound(String sound) {
		if (SoundManager.isLooping) {
			//SoundManager.loop.stop();
			
			return;
		}
		else {
		SoundManager.isLooping = true;
		
		SoundManager.loop = SoundManager.assetManager.getSound(sound);
		SoundManager.loop.loop(SoundManager.SystemVolume * LocalMusic.getSystemVolume());
		}
	}
	
	public static void setAssetManager(AssetManagerX assetManager) {
		SoundManager.assetManager = assetManager;
	}

	private static int random (int low, int high) {
		return (int) Math.round(Math.random() * (high - low) + low);
	}
}
