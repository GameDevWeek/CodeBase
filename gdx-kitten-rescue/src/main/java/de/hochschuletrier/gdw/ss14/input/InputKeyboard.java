package de.hochschuletrier.gdw.ss14.input;

import java.util.HashSet;

import com.badlogic.gdx.Input;

public class InputKeyboard extends InputDevice {

    private HashSet<Integer> pressedKeys = new HashSet<>();
    
    public InputKeyboard() {
        devicType = DeviceType.KEYBOARD;
    }
    
	@Override
	public boolean keyDown(int keycode) {
	    if (super.keyDown(keycode)) return true;
	    
	    switch (keycode) {
    		case Input.Keys.W:  
    		case Input.Keys.A: 
    		case Input.Keys.S: 
    		case Input.Keys.D:
    		    pressedKeys.add(keycode);
    			break;
    		case Input.Keys.K: 
    		    this.fireLaserButtonPressed();
    		    break; 
    		case Input.Keys.L: 
    		    this.fireWaterPistolButtonDown();
    		    break;
	    }
	    return false;
	}

	@Override
	public boolean keyUp(int keycode) {
	    if (super.keyUp(keycode)) return true;
	    
	    pressedKeys.remove(keycode);
	    
		switch (keycode) {
			case Input.Keys.L:
			    this.fireWaterPistolButtonUp();
			    break;
		}
		return false;
	}

    @Override
    public void update() {
        fireMoveAction();
    }
    
    private void fireMoveAction() {
        for (Integer keycode : this.pressedKeys) {
            switch (keycode) {
                case Input.Keys.W:
                    this.fireMoveUp(1.0f);
                    break;
                case Input.Keys.A:
                    this.fireMoveLeft(1.0f);
                    break;
                case Input.Keys.S:
                    this.fireMoveDown(1.0f);
                    break;
                case Input.Keys.D:
                    this.fireMoveRight(1.0f);
                    break;
            }
        }
    }
}
