package de.hochschuletrier.gdw.ss14.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;

public class UpdatePositionSystem extends IteratingSystem {

    protected final ComponentMapper<PhysixBodyComponent> physixMapper = ComponentMapper.getFor(PhysixBodyComponent.class);
    protected final ComponentMapper<PositionComponent> positionMapper = ComponentMapper.getFor(PositionComponent.class);

    public UpdatePositionSystem() {
        this(0);
    }

    public UpdatePositionSystem(int priority) {
        super(Family.getFor(PositionComponent.class, PhysixBodyComponent.class), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = physixMapper.get(entity);
        PositionComponent position = positionMapper.get(entity);
        position.x = physix.getX();
        position.y = physix.getY();
        position.rotation = physix.getAngle() * MathUtils.radiansToDegrees;
    }

}
