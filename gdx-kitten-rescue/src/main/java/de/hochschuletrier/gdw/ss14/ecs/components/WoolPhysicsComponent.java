package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Daniel Dreher on 02.10.2014.
 */
public class WoolPhysicsComponent extends PhysicsComponent
{
    private Vector2 initPosition;
    private float width;
    private float height;
    private float rotation;
    private float friction = 1.0f;
    private float restitution = 0.0f;

    public WoolPhysicsComponent(Vector2 position, float width, float height, float rotation)
    {
        this.initPosition = position;
        this.width = width;
        this.height = height;
        this.rotation = rotation;

    }

    @Override
    public void initPhysics(PhysixManager manager) {

        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1)
                .friction(friction).restitution(restitution);

        physicsBody = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager)
                .position(initPosition).fixedRotation(true).angle(rotation).create();

        physicsBody.setAngularVelocity(0);

        physicsBody.createFixture(fixturedef.shapeCircle(width/2, new Vector2(0,( height)/2)));
        //physicsBody.createFixture(fixturedef.shapeBox(width, height));
        setPhysicsBody(physicsBody);
        physicsBody.setOwner(this);
    }
}
