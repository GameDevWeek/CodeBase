package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixContactAdapter implements PhysixContactListener{

    public void beginContact(PhysixContact contact) {
    }

    public void endContact(PhysixContact contact) {
    }

    public void preSolve(PhysixContact contact, Manifold oldManifold) {
    }

    public void postSolve(PhysixContact contact, ContactImpulse impulse) {
    }
}
