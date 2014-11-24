package de.hochschuletrier.gdw.ss14.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss14.game.ComponentMappers;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;

public class UpdatePositionSystem extends IteratingSystem {

    public UpdatePositionSystem() {
        this(0);
    }

    public UpdatePositionSystem(int priority) {
        super(Family.all(PositionComponent.class, PhysixBodyComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        position.x = physix.getX();
        position.y = physix.getY();
        position.rotation = physix.getAngle() * MathUtils.radiansToDegrees;
    }

}
