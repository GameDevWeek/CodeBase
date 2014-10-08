package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PhysixComponent extends Component implements Pool.Poolable {

    public float x;
    public float y;

    @Override
    public void reset() {
        x = y = 0;
    }
}
