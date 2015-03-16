package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class SpawnComponent extends Component implements Pool.Poolable {

	public boolean isSpawnPoint;

	@Override
	public void reset() {
		isSpawnPoint = true;
	}

}
