package de.hochschuletrier.gdw.ss14.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

public class InputGamePad extends InputDevice implements ControllerListener {

	private HashMap<Integer, Float> pressedKeys = new HashMap<>();
	
	protected enum InputDirection {
	    UP, LEFT, DOWN, RIGHT
	}
	
    @Override
    public void update() {
    	for (Entry<Integer, Float> direction : this.pressedKeys.entrySet()) {
            switch (direction.getKey()) {
                case 0: // InputDirection.UP = 0
                    this.fireMoveUp(direction.getValue());
                    break;
                case 1: // InputDirection.LEFT = 1
                    this.fireMoveLeft(direction.getValue());
                    break;
                case 2: // InputDirection.DOWN = 2
                    this.fireMoveDown(direction.getValue());
                    break;
                case 3: // InputDirection.RIGHT = 3
                    this.fireMoveRight(direction.getValue());
                    break;
            }
        }        
    }

	@Override
	public void connected(Controller controller) {
		System.out.println("GamePad connected");		
	}

	@Override
	public void disconnected(Controller controller) {
		System.out.println("GamePad disconnected");
		
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		System.out.println("button");
		switch (buttonCode) {
			case 2: this.fireWaterPistolButtonDown(); break;	
			case 0: this.fireLaserButtonPressed(); break;
			case 7: this.fireMenuButtonPressed(); break;
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		switch (buttonCode) {
			case 2: this.fireWaterPistolButtonUp(); break;
		}
		
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (axisCode == 0) {
			if(value >= 0.2) {
				// this.fireMoveDown(value);
				pressedKeys.remove(InputDirection.UP.ordinal());
				pressedKeys.remove(InputDirection.DOWN.ordinal());
				pressedKeys.put(InputDirection.DOWN.ordinal(), value);
			}
			else if(value <= -0.2) {
				// this.fireMoveUp(value);
				pressedKeys.remove(InputDirection.DOWN.ordinal());
				pressedKeys.remove(InputDirection.UP.ordinal());
				pressedKeys.put(InputDirection.UP.ordinal(), value);
			}
			else {
				pressedKeys.remove(InputDirection.UP.ordinal());
				pressedKeys.remove(InputDirection.DOWN.ordinal());
			}
		}
		else if (axisCode == 1) {
			if(value >= 0.2) {
				pressedKeys.remove(InputDirection.LEFT.ordinal());
				pressedKeys.remove(InputDirection.RIGHT.ordinal());
				pressedKeys.put(InputDirection.RIGHT.ordinal(), value);
			}
			else if(value <= -0.2) {
				pressedKeys.remove(InputDirection.RIGHT.ordinal());
				pressedKeys.remove(InputDirection.LEFT.ordinal());
				pressedKeys.put(InputDirection.LEFT.ordinal(), value);
			}
			else {
				pressedKeys.remove(InputDirection.RIGHT.ordinal());
				pressedKeys.remove(InputDirection.LEFT.ordinal());
			}
		}
		// System.out.println(controller.getName() + " (axis moved) : " + axisCode + " - " + value);
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode,
			PovDirection value) {
		System.out.println(controller.getName() + " (pov moved) : " + povCode + " - " + value);
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode,
			boolean value) {
		System.out.println(controller.getName() + " (X_Slider moved) : " + sliderCode + " - " + value);
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode,
			boolean value) {
		System.out.println(controller.getName() + " (Y_Slider moved) : " + sliderCode + " - " + value);
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller,
			int accelerometerCode, Vector3 value) {
		System.out.println(controller.getName() + " (accelerometer moved) : " + accelerometerCode + " - " + value);
		return false;
	}
	
	

}
