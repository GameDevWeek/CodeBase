package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class TextureComponent extends Component implements Pool.Poolable {

	public Texture texture;
	
	@Override
	public void reset() {
		texture = null;
	}

}
