package de.hochschuletrier.gdw.ss14.input;

import java.util.LinkedList;

import de.hochschuletrier.gdw.ss14.sandbox.inputTest.GameInputAdapter;

public abstract class InputDevice {

	private LinkedList<GameInputAdapter> listener = new LinkedList<>(); 
	
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
}
