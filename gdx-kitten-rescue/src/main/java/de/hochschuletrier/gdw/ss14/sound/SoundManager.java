package de.hochschuletrier.gdw.ss14.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.*;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.states.GameStates;
import de.hochschuletrier.gdw.ss14.ui.UIActions;

public class SoundManager {
	private static Sound sound;
	private static AssetManagerX assetManager;
	
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
				switch(actionString) {
					case "BELLOVER":
						SoundManager.loadSound("click");
						SoundManager.sound.play();
						break;
				}
				break;
			default:
				break;
				
		}
	}
	
	private static void loadSound(String sound) {
		SoundManager.sound = SoundManager.assetManager.getSound(sound);
	}
	
	public static void setAssetManager(AssetManagerX assetManager) {
		SoundManager.assetManager = assetManager;
	}
}
