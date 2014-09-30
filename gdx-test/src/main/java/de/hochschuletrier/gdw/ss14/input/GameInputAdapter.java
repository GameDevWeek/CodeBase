package de.hochschuletrier.gdw.ss14.input;

public interface GameInputAdapter {
	
	/**
	 * Movement of the mouse on the screen
	 * 
	 * @param screenX X-Position in screen space
	 * @param screenY Y-Position in screen space
	 */
	public void mouseMoved(int screenX, int screenY);
	
	/**
	 * move up command from user
	 */
	public void moveUp();
	
	/**
	 * move down command from user
	 */
	public void moveDown();
	
	/**
	 * move left command from user
	 */
	public void moveLeft();
	
	/**
	 * move right command from user
	 */
	public void moveRight();
	
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
