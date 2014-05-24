package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author Santo Pfingsten
 */
class PhysixContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) {
            return;
        }

        PhysixBody objectA = (PhysixBody) fixA.getBody().getUserData();
        PhysixBody objectB = (PhysixBody) fixB.getBody().getUserData();

        objectA.beginContact(contact);
        objectB.beginContact(contact);

//        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
//        if (objectA != null) {
//            objectA.beginContact(contact);
//        }
//        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
//        if (objectB != null) {
//            objectB.beginContact(contact);
//        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) {
            return;
        }

        PhysixBody objectA = (PhysixBody) fixA.getBody().getUserData();
        PhysixBody objectB = (PhysixBody) fixB.getBody().getUserData();

        objectA.endContact(contact);
        objectB.endContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) {
            return;
        }

        PhysixBody objectA = (PhysixBody) fixA.getBody().getUserData();
        PhysixBody objectB = (PhysixBody) fixB.getBody().getUserData();

        objectA.preSolve(contact, oldManifold);
        objectB.preSolve(contact, oldManifold);

//        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
//        if (objectA != null) {
//            objectA.preSolve(contact, oldManifold);
//        }
//        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
//        if (objectB != null) {
//            objectB.preSolve(contact, oldManifold);
//        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA == null || fixB == null) {
            return;
        }

        PhysixBody objectA = (PhysixBody) fixA.getBody().getUserData();
        PhysixBody objectB = (PhysixBody) fixB.getBody().getUserData();

        objectA.postSolve(contact, impulse);
        objectB.postSolve(contact, impulse);

//        PhysixBody objectA = (PhysixBody) contact.getFixtureA().getBody().getUserData();
//        if (objectA != null) {
//            objectA.postSolve(contact, impulse);
//        }
//        PhysixBody objectB = (PhysixBody) contact.getFixtureB().getBody().getUserData();
//        if (objectB != null) {
//            objectB.postSolve(contact, impulse);
//        }
    }
}
