package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class LayerComponent extends Component implements Pool.Poolable {
	public int layer;
	public float parallax;
	
	@Override
	public void reset() {
		layer = 0;
	}
}
