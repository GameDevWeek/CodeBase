package de.hochschuletrier.gdw.ss14.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class InputMouse extends InputDevice implements InputProcessor {

	/**
	 * Mouse moved
	 * 
	 * @param screenX X-Position in screen space
	 * @param screenY Y-Position in screen space
	 */
	private void fireLaserMoved(int screenX, int screenY) {
		for(GameInputAdapter inp: listener) {
			inp.mouseMoved(screenX, screenY);
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
			case Input.Keys.ESCAPE: this.fireMenuButtonPressed(); break;// Menu keyboard
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return false;
	}

}
