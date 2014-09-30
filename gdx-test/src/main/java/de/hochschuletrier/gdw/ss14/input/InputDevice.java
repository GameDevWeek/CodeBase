package de.hochschuletrier.gdw.ss14.input;

import java.util.LinkedList;

public abstract class InputDevice {

	protected LinkedList<GameInputAdapter> listener = new LinkedList<>(); 
	
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
	 *  Laser on / off
	 */
	protected void fireLaserButtonPressed() {	
		for(GameInputAdapter inp: listener) {
			inp.laserButtonPressed();
		}
	}
	
	/**
	 * water pistol on
	 */
	protected void fireWaterPistolButtonDown() {
		for(GameInputAdapter inp: listener) {
			inp.waterPistolButtonDown();
		}
	}
	
	/**
	 * water pistol off
	 */
	protected void fireWaterPistolButtonUp() {
		for(GameInputAdapter inp: listener) {
			inp.waterPistolButtonUp();
		}
	}
	
	/**
	 * go to the menu or back
	 */
	protected void fireMenuButtonPressed() {
		for(GameInputAdapter inp: listener) {
			inp.menueButtonPressed();
		}
	}
}
