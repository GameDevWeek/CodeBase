package de.hochschuletrier.gdw.ss14.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import de.hochschuletrier.gdw.commons.gdx.state.*;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.states.GameStates;
import de.hochschuletrier.gdw.ss14.ui.UIActions;

public class SoundManager {
	private static Sound sound;
	private static AssetManager assetManager;
	
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
					case "BELLCLICKED":
						break;
				}
				break;
			default:
				break;
				
		}
	}
	
	public static void setAssetManager(AssetManager assetManager) {
		SoundManager.assetManager = assetManager;
	}
}
