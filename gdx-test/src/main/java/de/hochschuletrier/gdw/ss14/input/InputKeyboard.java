package de.hochschuletrier.gdw.ss14.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputKeyboard extends InputDevice implements InputProcessor {

	/**
	 * moved up
	 */
	private void fireMoveUp() {
		for(GameInputAdapter inp: listener) {
			inp.moveUp();
		}
	}
	
	/**
	 * moved down
	 */
	private void fireMoveDown() {
		for(GameInputAdapter inp: listener) {
			inp.moveDown();
		}
	}
	
	/**
	 * moved left
	 */
	private void fireMoveLeft() {
		for(GameInputAdapter inp: listener) {
			inp.moveLeft();
		}
	}
	
	/**
	 * moved right
	 */
	private void fireMoveRight() {
		for(GameInputAdapter inp: listener) {
			inp.moveRight();
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.W: this.fireMoveUp(); break; 
		case Input.Keys.A: this.fireMoveLeft(); break;
		case Input.Keys.S: this.fireMoveDown(); break;
		case Input.Keys.D: this.fireMoveRight(); break;
			
		case Input.Keys.K: this.fireLaserButtonPressed(); break; // LaserButton keyboard
		case Input.Keys.L: this.fireWaterPistolButtonDown(); break; // waterPistol keyboard
		
		case Input.Keys.ESCAPE: this.fireMenuButtonPressed(); break; // Menu keyboard
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
