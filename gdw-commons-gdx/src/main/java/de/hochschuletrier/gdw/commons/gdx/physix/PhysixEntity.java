package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * @author Santo Pfingsten
 */
public abstract class PhysixEntity {

    protected PhysixBody physicsBody;

    public abstract void initPhysics(PhysixManager manager);

    protected void setPhysicsBody(PhysixBody physicsObject) {
        physicsObject.setOwner(this);
        this.physicsBody = physicsObject;
    }

    public Vector2 getPosition() {
        return physicsBody.getPosition();
    }

    public Vector2 getVelocity() {
        return physicsBody.getLinearVelocity();
    }

    public void setVelocity(Vector2 velocity) {
        physicsBody.setLinearVelocity(velocity);
    }

    public void setVelocity(float x, float y) {
        physicsBody.setLinearVelocity(x, y);
    }

    public void setVelocityX(float x) {
        physicsBody.setLinearVelocityX(x);
    }

    public void setVelocityY(float y) {
        physicsBody.setLinearVelocityY(y);
    }

    protected void beginContact(PhysixContact contact) {
    }

    protected void endContact(PhysixContact contact) {
    }

    protected void preSolve(PhysixContact contact, Manifold oldManifold) {
    }

    protected void postSolve(PhysixContact contact, ContactImpulse impulse) {
    }
}
