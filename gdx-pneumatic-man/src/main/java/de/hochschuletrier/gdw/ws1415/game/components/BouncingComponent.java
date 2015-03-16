package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BouncingComponent extends Component implements Pool.Poolable {
	public float bouncingImpulse;
	// time between bounces in seconds
	public float restingTime, timeToNextBounce;
	public boolean bouncing;
	
	public BouncingComponent(float bouncingImpulse){
		bouncing = false;
		this.bouncingImpulse = bouncingImpulse;
	}

	@Override
	public void reset() {
		bouncingImpulse = 0;
	}
}
