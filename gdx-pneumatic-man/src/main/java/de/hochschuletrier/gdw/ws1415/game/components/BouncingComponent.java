package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Use this to make an entity bounce. Increase the bouncingImpulse to increase
 * the height of the bounce, increase the restingTime to increase the time
 * between bounces.
 * 
 * @author David
 *
 */
public class BouncingComponent extends Component implements Pool.Poolable {

	public float bouncingImpulse;
	// time between bounces in seconds
	public float restingTime;

	public float timeToNextBounce;

	/**
	 * 
	 * @param bouncingImpulse
	 *            increase, to increase bounce height
	 * @param restingTime
	 *            increase, to increase time between bounces
	 */
	public BouncingComponent(float bouncingImpulse, float restingTime) {
		this.restingTime = restingTime;
		this.bouncingImpulse = bouncingImpulse;

		timeToNextBounce = 0;
	}

	@Override
	public void reset() {
		bouncingImpulse = 0;
		restingTime = 0;
		timeToNextBounce = 0;
	}
}
