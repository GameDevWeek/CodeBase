package de.hochschuletrier.gdw.ws1415.game.input;

import com.badlogic.gdx.Gdx;

public class InputManager  {

	public void startListenKeyboard() {
		InputKeybord inputProcessor = new InputKeybord();
		Gdx.input.setInputProcessor(inputProcessor);	
	}
	
	public void startListenGamepad() {
	    // TODO
	}
}
