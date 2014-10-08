package de.hochschuletrier.gdw.ss14.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.physix.AbstractPhysixComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;

public class BallPhysixComponent extends AbstractPhysixComponent {

    private Vector2 origin;
    private float radius;

    public BallPhysixComponent(Vector2 origin, float radius) {
        this.origin = origin.cpy();
        this.radius = radius;
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void initPhysics(PhysixSystem manager) {
        physicsBody = new PhysixBodyDef(BodyType.DynamicBody, manager)
                .position(origin).fixedRotation(false).create();
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(manager)
                .density(5).friction(0.2f).restitution(0.4f).shapeCircle(radius);
        physicsBody.createFixture(fixtureDef);
//        physicsBody.setOwner(null);
        origin = null;
    }
}
