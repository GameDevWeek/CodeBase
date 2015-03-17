package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;


public class CameraSystem {
	private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
	private Vector3 cameraPos;
	
	public LimitedSmoothCamera getCamera() {
		return camera;
	}
	
	/**
	 * 
	 * Applies parallax by adjusting the camera position. 
	 * Doesn't do anything if the entity has no LayerComponent.
	 */
	void preParallax(Entity entity) {
		LayerComponent layer = ComponentMappers.layer.get(entity);
		if(layer == null)
			return;
		
		cameraPos = camera.getPosition();	
		
		camera.setDestination(cameraPos.x*layer.parallax, cameraPos.y*layer.parallax);
		camera.updateForced();
	}
	
	/**
	 * Sets the camera position back to the old camera position.
	 */
	void postParallax() {
		camera.setDestination(cameraPos.x, cameraPos.y);
		camera.updateForced();
	}
}
