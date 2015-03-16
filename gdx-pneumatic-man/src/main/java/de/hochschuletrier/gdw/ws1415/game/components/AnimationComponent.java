package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

public class AnimationComponent extends Component implements Pool.Poolable {

    public AnimationExtended animation;
    public float stateTime;
    public int layer;

    @Override
    public void reset() {
        animation = null;
        stateTime = 0;
        layer = 0;
    }
}
