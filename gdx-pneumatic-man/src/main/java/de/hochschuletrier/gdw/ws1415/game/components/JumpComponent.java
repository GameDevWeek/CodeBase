package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Use this Component to make an entity jump
 * 
 * @author David
 *
 */
public class JumpComponent extends Component implements Pool.Poolable {

	public float jumpImpulse;
	public float restingTime, timeToNextJump;
	
	public boolean doJump;
	
	public JumpComponent(){
		this(0,0);
	}

	/**
	 * 
	 * @param jumpImpulse
	 *            increase, to increase jump height
	 * @param restingTime
	 *            increase, to increase time between jumps
	 */
	public JumpComponent(float jumpImpulse, float restingTime) {
		this.jumpImpulse = jumpImpulse;
		this.restingTime = restingTime;
		
		timeToNextJump = 0;
		doJump = false;
	}
	
	/**
	 * call this to make the entity jump
	 */
	public void jump(){
		doJump = true;
	}

	@Override
	public void reset() {
		jumpImpulse = 0;
		restingTime = 0;
		doJump = false;
	}

}
