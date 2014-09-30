package de.hochschuletrier.gdw.ss14.sandbox.ecs.components;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class BallPhysicsComponent extends PhysicsComponent
{

    public Vector2 origin;
    public float radius;

    public BallPhysicsComponent(float x, float y, float radius)
    {
        origin = new Vector2(x, y);
        this.radius = radius;
    }

    @Override
    public void initPhysics(PhysixManager manager)
    {
        super.initPhysics(manager);

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager).position(origin)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(5).friction(0.2f).restitution(0.4f).shapeCircle(radius));
        setPhysicsBody(body);
    }
}
