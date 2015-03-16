package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SpawnComponent extends Component implements Pool.Poolable {

	public float x;
	public float y;

	@Override
	public void reset() {
		x = y = 0;
	}

}
