package de.hochschuletrier.gdw.commons.gdx.ashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Imagine a SortedIteratingSystem that you can add SubSystems to.
 * For each entity being processed, all SubSystems that match the entities family will be called in the order they have been added.
 *
 * @author Santo Pfingsten
 */
public class SortedSubIteratingSystem extends SortedIteratingSystem {
    
    public static abstract class SubSystem {

        private final Family family;

        public SubSystem(Family family) {
            this.family = family;
        }

        public abstract void processEntity(Entity entity, float deltaTime);
    }

    private final ArrayList<SubSystem> subSystems = new ArrayList();

    public SortedSubIteratingSystem(Family family, Comparator<Entity> comparator) {
        super(family, comparator);
    }

    public SortedSubIteratingSystem(Family family, Comparator<Entity> comparator, int priority) {
        super(family, comparator, priority);
    }

    public void addSubSystem(SubSystem worker) {
        subSystems.add(worker);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        for (SubSystem subSystem : subSystems) {
            if (subSystem.family.matches(entity)) {
                subSystem.processEntity(entity, deltaTime);
            }
        }
    }
}
