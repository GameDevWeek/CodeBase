package de.hochschuletrier.gdw.ss14.sound;

import java.util.EnumMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.states.*;

public class MusicManager {
	private static MusicManager Instance;
	private EnumMap<GameStateEnum, LocalMusic> musicStreamsList;
	private AssetManagerX assetManager;

	
	public static MusicManager getInstance() {
		if (MusicManager.Instance == null)
			MusicManager.Instance = new MusicManager();
		return MusicManager.Instance;
	}
	
	public MusicManager() {}
	
	public void init(AssetManagerX assetManager) {
		this.musicStreamsList = new EnumMap<GameStateEnum, LocalMusic> (GameStateEnum.class);
		this.assetManager = assetManager;
		this.fillStreamList();
	}
	
	private void fillStreamList() {
		for (GameStateEnum state : GameStateEnum.values()) {
			this.musicStreamsList.put(state, new LocalMusic(this.assetManager));
		}
	}
	
	public LocalMusic getMusicStreamByStateName(GameStateEnum stateName) {
		if (this.musicStreamsList.containsKey(stateName)) 
			return this.musicStreamsList.get(stateName);
		else
			return null;
	}
	
	public void stopAllStreams() {
		for (GameStateEnum state : GameStateEnum.values()) {
			if (this.musicStreamsList.get(state).isMusicPlaying())
				this.musicStreamsList.get(state).stop();
		}		
	}
	
	public void sendVolumeToStreams(float systemVolume) {
		for (GameStateEnum state : GameStateEnum.values()) {
			if (this.musicStreamsList.get(state).isMusicPlaying() && state.isActive()) {
				this.musicStreamsList.get(state).setVolume(systemVolume);
				System.out.println("STREAM + "+ state.isActive());
			}
		}		
	}
}
