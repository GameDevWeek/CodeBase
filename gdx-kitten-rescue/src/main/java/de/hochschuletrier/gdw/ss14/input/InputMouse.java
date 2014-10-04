package de.hochschuletrier.gdw.ss14.input;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.input.infos.InputInfo;
import de.hochschuletrier.gdw.ss14.input.infos.InputSettings;

public class InputMouse extends InputDevice {
    private HashMap<String, InputAction> inputMapping;
    
    public InputMouse() {
        devicType = DeviceType.MOUSE;
        readMapping();
    }
    
    private void readMapping() {
        inputMapping = new HashMap<>();
        InputInfo info = InputSettings.getInstance().getInputInfo(DeviceType.MOUSE);
        inputMapping.put(info.TOGGLE_LASER, InputAction.TOGGLE_LASER);
        inputMapping.put(info.SHOOT, InputAction.SHOOT);
    }
    

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (super.touchDown(screenX, screenY, pointer, button)) return true;
		
	    // hier könnte es bei einem Touch-Eingabegerät Probleme geben, 
	    // da bei diesem immer die linke Maustaste als gedrückt gilt
	    InputAction action = null;
	    switch (button) {
			case Input.Buttons.LEFT:
			    action = inputMapping.get("LEFT");
			    break;
			case Input.Buttons.RIGHT: 
			    action = inputMapping.get("RIGHT");
			    break;
		}
	    
	    switch (action) {
            case SHOOT:
                fireWaterPistolButtonDown();
                break;
            case TOGGLE_LASER:
                fireLaserButtonPressed();
                break;
        default:
            // nothing
            break;
	    }
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (super.touchUp(screenX, screenY, pointer, button)) return true;
		
	    switch (button) {
			case Input.Buttons.RIGHT:
			    if (inputMapping.get("RIGHT") == InputAction.SHOOT) {
			        this.fireWaterPistolButtonUp();
			    }
			case Input.Buttons.LEFT:
			    if (inputMapping.get("LEFT") == InputAction.SHOOT) {
                    this.fireWaterPistolButtonUp();
                } 
			    break;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (super.touchDragged(screenX, screenY, pointer)) return true;
		
	    this.fireMove(screenX, screenY);
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (super.mouseMoved(screenX, screenY)) return true;
		
	    this.fireMove(screenX, screenY);
		return false;
	}

    @Override
    public void update() {
        // nothing to do
    }
    
    public void setCursorPosition(int x, int y) {
        Gdx.input.setCursorPosition(x, y);
    }
    
    public Vector2 getCursorPosition() {
        return new Vector2(Gdx.input.getX(), Gdx.input.getY()); 
    }
    
    public int getX() {
        return Gdx.input.getX();
    }
    
    public int getY() {
        return Gdx.input.getY();
    }
}
