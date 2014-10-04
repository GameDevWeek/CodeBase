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
import de.hochschuletrier.gdw.ss14.states.GameStateEnum;
import de.hochschuletrier.gdw.ss14.ui.UIActions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundManager {
	private static Sound sound;
	private static Sound loop;
	private static Logger Logger = LoggerFactory.getLogger(SoundManager.class);
	private static boolean isLooping;
	private static AssetManagerX assetManager;
	public static float SystemVolume = 1.9f;
	
	public static void performAction(Enum action) {
		GameStateEnum actualGamestate = null;
		String actionString = action.name();

		for (GameStateEnum state : GameStateEnum.values()) {
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
					case "ON":
						SoundManager.playSound("gp_laser_on_temp");
						break;
					case "OFF":
						SoundManager.playSound("gp_laser_off_temp");
						break;
					case "FALL":
						Integer tmp = random(0,1);
						switch (tmp) {
							case 0: SoundManager.playSound("gp_cat_fall1"); break;
							case 1: SoundManager.playSound("gp_cat_fall2"); break;
						}
						break;
					case "JUMP":
						SoundManager.playSound("gp_cat_jump");
				} 
				break;
			default:
				break;
				
		}
	}

	public static void playSound(String sound) {
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
