package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * @author Santo Pfingsten
 */
public abstract class PhysixEntity {

    public PhysixBody physicsBody = null;

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
    
    public void setRotation(float r){
        physicsBody.setTransform(physicsBody.getPosition().x, physicsBody.getPosition().y, r);
    }
    
    public float getRotation() {
        return physicsBody.getAngle();
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
