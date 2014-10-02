package de.hochschuletrier.gdw.ss14.ecs.components;

/**
 * Component for Entities with Shadow
 * 
 * @author David Liebemann
 *
 */
public class ShadowComponent implements Component{
	// shadow alpha
	public float alpha = 0f;
	
	/**
	 *  z-Index varies size of jumping while jumping / falling
	 *  z > 1 : Shadow gets bigger
	 *  z < 1 : Shadow gets smaller
	 */
	public float z = 0.7f;
	
}
