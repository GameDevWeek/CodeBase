package de.hochschuletrier.gdw.ss14.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

import de.hochschuletrier.gdw.commons.gdx.state.*;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.states.GameStates;

public class SoundManager {
	private static Sound sound;
	private static AssetManager assetManager;
	
	public static void performAction(int and) {
		GameState actualGamestate = GameStates.GAMEPLAY.get();

		for (GameStates state : GameStates.values()) {
			if (state.isActive())
				continue;
		}
	}
	
	public static void setAssetManager(AssetManager assetManager) {
		SoundManager.assetManager = assetManager;
	}
}
