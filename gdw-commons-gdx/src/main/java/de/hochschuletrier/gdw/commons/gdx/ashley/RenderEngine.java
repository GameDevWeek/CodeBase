package de.hochschuletrier.gdw.commons.gdx.ashley;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.Comparator;

/**
 *
 * @author Santo Pfingsten
 */
public class RenderEngine {

    private static SystemComparator comparator = new SystemComparator();

    /**
     * An unordered list of RenderSystem
     */
    private Array<RenderSystem> systems;
    /**
     * An unordered and immutable list of RenderSystem
     */
    private ImmutableArray<RenderSystem> immutableSystems;
    /**
     * A hashmap that organises RenderSystems by class for easy retrieval
     */
    private ObjectMap<Class<?>, RenderSystem> systemsByClass;

    /**
     * Whether or not the engine is ticking
     */
    private boolean rendering;

    public RenderEngine() {
        systems = new Array<RenderSystem>(false, 16);
        immutableSystems = new ImmutableArray<RenderSystem>(systems);
        systemsByClass = new ObjectMap<Class<?>, RenderSystem>();

        rendering = false;
    }

    /**
     * Adds the {@link RenderSystem} to this Engine.
     */
    public void addSystem(RenderSystem system) {
        Class<? extends RenderSystem> systemType = system.getClass();

        if (!systemsByClass.containsKey(systemType)) {
            systems.add(system);
            systemsByClass.put(systemType, system);
            system.addedToEngine(this);

            systems.sort(comparator);
        }
    }

    /**
     * Removes the {@link RenderSystem} from this Engine.
     */
    public void removeSystem(RenderSystem system) {
        if (systems.removeValue(system, true)) {
            systemsByClass.remove(system.getClass());
            system.removedFromEngine(this);
        }
    }

    /**
     * Quick {@link RenderSystem} retrieval.
     */
    @SuppressWarnings("unchecked")
    public <T extends RenderSystem> T getSystem(Class<T> systemType) {
        return (T) systemsByClass.get(systemType);
    }

    /**
     * @return immutable array of all entity systems managed by the
     * {@link Engine}.
     */
    public ImmutableArray<RenderSystem> getSystems() {
        return immutableSystems;
    }

    /**
     * Renders all the systems in this Engine.
     */
    public void render() {
        rendering = true;
        for (int i = 0; i < systems.size; i++) {
            if (systems.get(i).checkProcessing()) {
                systems.get(i).render();
            }
        }

        rendering = false;
    }
    
    private static class SystemComparator implements Comparator<RenderSystem> {

        @Override
        public int compare(RenderSystem a, RenderSystem b) {
            return a.priority > b.priority ? 1 : (a.priority == b.priority) ? 0 : -1;
        }
    }
}
