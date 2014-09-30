package de.hochschuletrier.gdw.ss14.input;

public interface GameInputAdapter {
	
	/**
	 * Movement of the mouse on the screen
	 * 
	 * @param screenX X-Position in screen space
	 * @param screenY Y-Position in screen space
	 */
	public void move(int screenX, int screenY);
	
	/**
	 * move up command from user
	 */
	public void moveUp(float scale);
	
	/**
	 * move down command from user
	 */
	public void moveDown(float scale);
	
	/**
	 * move left command from user
	 */
	public void moveLeft(float scale);
	
	/**
	 * move right command from user
	 */
	public void moveRight(float scale);
	
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
