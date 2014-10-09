package de.hochschuletrier.gdw.commons.gdx.physix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool.Poolable;
import java.util.ArrayList;

/**
 * @author Santo Pfingsten
 */
public final class PhysixModifierComponent extends Component implements Poolable {

    public final ArrayList<Runnable> runnables = new ArrayList();

    @Override
    public void reset() {
        runnables.clear();
    }
    
    public void schedule(Runnable runnable) {
        runnables.add(runnable);
    }
}
