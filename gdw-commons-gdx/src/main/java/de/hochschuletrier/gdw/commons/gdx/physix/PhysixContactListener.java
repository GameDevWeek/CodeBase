package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Santo Pfingsten
 */
public interface PhysixContactListener {

    void beginContact(PhysixContact contact);

    void endContact(PhysixContact contact);

    void preSolve(PhysixContact contact, Manifold oldManifold);

    void postSolve(PhysixContact contact, ContactImpulse impulse);
}
