package de.hochschuletrier.gdw.ss14.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss14.game.components.DeletedComponent;

public class EntityRemovalSystem extends IteratingSystem {

    private Engine engine;

    public EntityRemovalSystem() {
        this(0);
    }

    public EntityRemovalSystem(int priority) {
        super(Family.getFor(DeletedComponent.class), priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        entity.removeAll();
        engine.removeEntity(entity);
    }

}
