package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;

public class HealthComponent extends Component implements Poolable {

    public int Value;

    @Override
    public void reset() {
        Value = 10;
    }

}
