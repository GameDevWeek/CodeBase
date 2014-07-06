package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 *
 * @author Santo Pfingsten
 */
public class Player extends PhysixEntity {

    private final Vector2 origin = new Vector2();

    public Player(float x, float y) {
        origin.set(x, y);
    }

    public void initPhysics(PhysixManager manager) {
        PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
                .position(origin).fixedRotation(true).create();
        PhysixUtil.createCapsuleBody(body, new PhysixFixtureDef(manager).density(1).friction(0.5f), 100, 30);
        setPhysicsBody(body);
    }
}
