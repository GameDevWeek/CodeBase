package de.hochschuletrier.gdw.ss14.sandbox.test;

public interface GameInputAdapter {
	
	/**
	 * Movement of the laser on the screen
	 * 
	 * @param screenX X-Position in screen space
	 * @param screenY Y-Position in screen space
	 */
	public void laserMoved(int screenX, int screenY);
	
	/**
	 * Laser on / off
	 */
	public void laserButtonPressed();
	
	/**
	 * water pistol on
	 */
	public void waterPistolButtonDown();
	
	/**
	 * water pistol off
	 */
	public void waterPistolButtonUp();
	
	/**
	 * go to menu or go back to the game
	 */
	public void menueButtonPressed();
	
}
