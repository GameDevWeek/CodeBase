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
	
	private static int hundBellenVerzoegerung = 0;
	
	public static void performAction(Enum action) {
		GameStateEnum actualGamestate = null;
		String actionString = action.name();
		Integer tmp;
		
		for (GameStateEnum state : GameStateEnum.values()) {
			if (state.isActive()) {
				actualGamestate = state;
				break;
			}
		}
		
		switch(actualGamestate) {
			case MAINMENU:
			case OPTIONSMENU:
			case LEVELMENU:
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
						tmp = random(0,1);
						switch (tmp) {
							case 0: SoundManager.playSound("gp_cat_fall1"); break;
							case 1: SoundManager.playSound("gp_cat_fall2"); break;
						}
						break;
					case "JUMP":
						SoundManager.playSound("gp_cat_jump"); break;
					case "JUMPING_IN_BOX": 
						tmp = random(0,1);
						switch (tmp) {
							case 0: SoundManager.playSound("gp_cat_box_in1"); break;
							case 1: SoundManager.playSound("gp_cat_box_in2"); break;
						}
						break;
					case "KILLING": 
						tmp = random(0,2);
						switch (tmp) {
							case 0: SoundManager.playSound("gp_dog_bite1"); break;
							case 1: SoundManager.playSound("gp_dog_bite2"); break;
							case 2: SoundManager.playSound("gp_dog_bite3"); break;
						}
						break;
					case "SUCCESS": // muss noch geändert werden
						System.out.println(hundBellenVerzoegerung);
						if(hundBellenVerzoegerung > 60) {							
							tmp = random(0,7);
							switch (tmp) {
								case 0: SoundManager.playSound("gp_dogAngry01"); break;
								case 1: SoundManager.playSound("gp_dogAngry02"); break;
								case 2: SoundManager.playSound("gp_dogAngry03"); break;
								case 3: SoundManager.playSound("gp_dogAngry04"); break;
								case 4: SoundManager.playSound("gp_dogAngry05"); break;
								case 5: case 6: case 7: break; // nichts machen !!
							}
							hundBellenVerzoegerung = 0;
						}
						else {
							hundBellenVerzoegerung++;
						}
						
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
