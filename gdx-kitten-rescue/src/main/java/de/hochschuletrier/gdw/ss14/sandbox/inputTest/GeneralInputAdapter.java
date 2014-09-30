package de.hochschuletrier.gdw.ss14.sandbox.inputTest;

import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class GeneralInputAdapter implements InputProcessor {
	
	private LinkedList<GameInputAdapter> listener = new LinkedList<>(); 
	
	/**
	 * Constructor
	 */
	public GeneralInputAdapter() {
	}
	
	/**
	 * 
	 * @param gia @ GameInputAdapter to the Listener
	 */
	public void addGameInputAdapter(GameInputAdapter gia) {
		listener.add(gia);
	}
	
	/**
	 * 
	 * @param gia remove GameInputAdapter gia from the listener
	 */
	public void removeGameInputAdapter(GameInputAdapter gia) {
		listener.remove(gia);
	}
	
	/**
	 * 
	 * @param screenX X-Position in screen space
	 * @param screenY Y-Position in screen space
	 */
	private void fireLaserMoved(int screenX, int screenY) {	
		for(GameInputAdapter inp: listener) {
			inp.laserMoved(screenX, screenY);
		}
	}
	
	/**
	 *  Laser on / off
	 */
	private void fireLaserButtonPressed() {	
		for(GameInputAdapter inp: listener) {
			inp.laserButtonPressed();
		}
	}
	
	/**
	 * water pistol on
	 */
	private void fireWaterPistolButtonDown() {
		for(GameInputAdapter inp: listener) {
			inp.waterPistolButtonDown();
		}
	}
	
	/**
	 * water pistol off
	 */
	private void fireWaterPistolButtonUp() {
		for(GameInputAdapter inp: listener) {
			inp.waterPistolButtonUp();
		}
	}
	
	/**
	 * go to the menu or back
	 */
	private void fireMenuButtonPressed() {
		for(GameInputAdapter inp: listener) {
			inp.menueButtonPressed();
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.W: break;// must be checked with the other teams !!!! 
			case Input.Keys.A: break;// must be checked with the other teams !!!! 
			case Input.Keys.S: break;// must be checked with the other teams !!!! 
			case Input.Keys.D: break;// must be checked with the other teams !!!! 
				
			case Input.Keys.K: this.fireLaserButtonPressed(); break;// LaserButton keyboard
			case Input.Keys.L: this.fireWaterPistolButtonDown(); break;// waterPistol keyboard
			
			case Input.Keys.ESCAPE: this.fireMenuButtonPressed(); break;// Menu keyboard
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
			case Input.Keys.L: this.fireWaterPistolButtonUp(); break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// no need
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switch (button) {
			case Input.Buttons.LEFT: this.fireLaserButtonPressed(); break;
			case Input.Buttons.RIGHT: this.fireWaterPistolButtonDown(); break;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		switch (button) {
			case Input.Buttons.RIGHT: this.fireWaterPistolButtonUp(); break;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		this.fireLaserMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		this.fireLaserMoved(screenX, screenY);
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// no need
		return false;
	}	
}
