package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Santo Pfingsten
 */
class PhysixContactListenerInternal implements ContactListener {

    private final PhysixContact physixContact = new PhysixContact();

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
//            physixContact.run(contact, (owner)->owner.beginContact(physixContact));
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
//            physixContact.run(contact, (owner)->owner.endContact(physixContact));
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
//            physixContact.run(contact, (owner)->owner.preSolve(physixContact, oldManifold));
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
//            physixContact.run(contact, (owner)->owner.postSolve(physixContact, impulse));
        }
    }
}
