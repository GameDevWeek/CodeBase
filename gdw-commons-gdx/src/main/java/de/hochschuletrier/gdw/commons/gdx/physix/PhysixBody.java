package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;


/**
 *
 * @author Santo Pfingsten
 */
public final class PhysixBody {

    private final PhysixManager manager;
    private PhysixEntity owner;
    private final Body body;
    private static final Vector2 dummyVector = new Vector2();
    private final Vector2 linearVelocity = new Vector2();
    private final Vector2 position = new Vector2();

    protected PhysixBody(BodyDef bodyDef, PhysixManager manager) {
        this.manager = manager;

        body = manager.getWorld().createBody(bodyDef);
        body.setUserData(this);
    }

    public Fixture createFixture(PhysixFixtureDef fixtureDef) {
        return body.createFixture(fixtureDef);
    }

    public Body getBody() {
        return body;
    }

    /**Don't change the List !*/
    public Array<Fixture> getFixtureList() {
        return body.getFixtureList();
    }

    public void setOwner(PhysixEntity owner) {
        this.owner = owner;
    }

    public PhysixEntity getOwner() {
        return owner;
    }

    public float getX() {
        return manager.toWorld(body.getPosition().x);
    }

    public float getY() {
        return manager.toWorld(body.getPosition().y);
    }

    public Vector2 getPosition() {
        return manager.toWorld(body.getPosition(), position);
    }

    public void setX(float x) {
        body.setTransform(dummyVector.set(manager.toBox2D(x), body.getPosition().y), body.getAngle());
    }

    public void setY(float y) {
        body.setTransform(dummyVector.set(body.getPosition().x, manager.toBox2D(y)), body.getAngle());
    }

    public void setPosition(Vector2 pos) {
        body.setTransform(manager.toBox2D(pos, dummyVector), body.getAngle());
    }

    public void setPosition(float x, float y) {
        body.setTransform(manager.toBox2D(x, y, dummyVector), body.getAngle());
    }

    public void setTransform(float x, float y, float omega) {
        body.setTransform(manager.toBox2D(x, y, dummyVector), omega);
    }

    public Vector2 getLocalCenter(Vector2 origin) {
        return manager.toBox2D(body.getLocalCenter(), origin);
    }

    public void simpleForceApply(Vector2 force) {
        body.applyForceToCenter(force, true);
    }

    public void applyImpulse(Vector2 speed) {
        body.applyLinearImpulse(manager.toBox2D(speed, dummyVector), body.getWorldCenter(), true);
    }

    public void applyImpulse(float x, float y) {
        body.applyLinearImpulse(manager.toBox2D(x, y, dummyVector), body.getWorldCenter(), true);
    }

    public float getAngle() {
        return body.getAngle();
    }

    public Vector2 getLinearVelocity() {
        return manager.toWorld(body.getLinearVelocity(), linearVelocity);
    }

    public void setLinearVelocity(Vector2 v) {
        body.setLinearVelocity(manager.toBox2D(v, dummyVector));
    }

    public void setLinearVelocity(float x, float y) {
        body.setLinearVelocity(dummyVector.set(manager.toBox2D(x), manager.toBox2D(y)));
        body.setAwake(true);
    }

    public void setLinearVelocityX(float x) {
        body.setLinearVelocity(dummyVector.set(manager.toBox2D(x), body.getLinearVelocity().y));
        body.setAwake(true);
    }

    public void setLinearVelocityY(float y) {
        body.setLinearVelocity(dummyVector.set(body.getLinearVelocity().x, manager.toBox2D(y)));
        body.setAwake(true);
    }

    public void setAngularVelocity(float omega) {
        body.setAngularVelocity(omega);
        body.setAwake(true);
    }

    public boolean isAwake() {
        return body.isAwake();
    }

    public boolean isAsleep() {
        return !body.isAwake();
    }

    public void setGravityScale(float gravityScale) {
        body.setGravityScale(gravityScale);
    }

    public float getGravityScale() {
        return body.getGravityScale();
    }

    public void setMassData(MassData massData) {
        body.setMassData(massData);
    }

    public void setMassData(float mass) {
        MassData massData = new MassData();
        massData.mass = mass;
        body.setMassData(massData);
    }

    public float getMass() {
        return body.getMass();
    }

    public void setActive(boolean value) {
        body.setActive(value);
    }

    public void setAwake(boolean value) {
        body.setAwake(value);
    }

    public BodyDef.BodyType getBodyType() {
        return body.getType();
    }

    public float getLinearDamping() {
        return body.getLinearDamping();
    }

    public void setLinearDamping(float linearDamping) {
        body.setLinearDamping(linearDamping);
    }

    public void scale(float scale) {
        for (Fixture f : getFixtureList()) {
            Shape shape = f.getShape();
            float s = shape.getRadius();
            shape.setRadius(s * scale);

        }
    }
}
