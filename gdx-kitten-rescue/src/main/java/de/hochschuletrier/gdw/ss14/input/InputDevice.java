package de.hochschuletrier.gdw.ss14.input;

import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import de.hochschuletrier.gdw.ss14.Main;

public abstract class InputDevice extends InputAdapter {

    public static enum DeviceType {
        MOUSE,
        KEYBOARD,
        GAMEPAD
    };
    
	protected LinkedList<GameInputAdapter> listener = new LinkedList<>();
	protected DeviceType devicType;
	protected boolean isRegistred = false;
	
	public DeviceType getDeviceType() {
	    return devicType;
	}
	
	public boolean isRegistred() {
	    return isRegistred;
	}
	
	public void registerProcessor() {
	    if (!isRegistred) {
	        Main.inputMultiplexer.addProcessor(this);
	        isRegistred = true;
	    }
	}
	
	public void unregisterProcessor() {
	    if (isRegistred) {
    	    Main.inputMultiplexer.removeProcessor(this);
    	    isRegistred = false;
	    }
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
    
    public void clearAllGameInputAdapter() {
        listener.clear();
    }
    
    public abstract void update();
    
    /**
	 *  Laser on / off
	 */
	protected void fireLaserButtonPressed() {
	    //System.out.println("LaserButtonPressed");
		for(GameInputAdapter inp: listener) {
			inp.laserButtonPressed();
		}
	}
	
	/**
	 * water pistol on
	 */
	protected void fireWaterPistolButtonDown() {
	    //System.out.println("WaterPistolButtonDown");
		for(GameInputAdapter inp: listener) {
			inp.waterPistolButtonDown();
		}
	}
	
	/**
	 * water pistol off
	 */
	protected void fireWaterPistolButtonUp() {
	    //System.out.println("WaterPistolButtonUp");
		for(GameInputAdapter inp: listener) {
			inp.waterPistolButtonUp();
		}
	}
	
	/**
	 * go to the menu or back
	 */
	protected void fireMenuButtonPressed() {
		//System.out.println("MenueButtonPressed");
	    for(GameInputAdapter inp: listener) {
			inp.menueButtonPressed();
		}
	}
	
	protected void fireMove(int screenX, int screenY) {
	    //System.out.println("Move: x = " + screenX + " | y = " +screenY);
        for(GameInputAdapter inp: listener) {
            inp.move(screenX, screenY);
        }
    }
	
	protected void fireMoveUp(float scale) {
	    //System.out.println("MoveUp: scale = " + scale);
        for(GameInputAdapter inp: listener) {
            inp.moveUp(scale);
        }
    }
	
	protected void fireMoveDown(float scale) {
	    //System.out.println("MoveDown: scale = " + scale);
        for(GameInputAdapter inp: listener) {
            inp.moveDown(scale);
        }
    }
	
	protected void fireMoveLeft(float scale) {
	    //System.out.println("MoveLeft: scale = " + scale);
        for(GameInputAdapter inp: listener) {
            inp.moveLeft(scale);
        }
    }
	
	protected void fireMoveRight(float scale) {
	    //System.out.println("MoveRight: scale = " + scale);
        for(GameInputAdapter inp: listener) {
            inp.moveRight(scale);
        }
    }
	
	// Allgemeine Inputs:
	@Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE: 
                this.fireMenuButtonPressed();
                break;// Menu keyboard
        }
        return false;
    }
}
