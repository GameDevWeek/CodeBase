package de.hochschuletrier.gdw.commons.gdx.physix.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;

/**
 * @author Santo Pfingsten
 */
public final class PhysixBodyComponent extends Component implements Poolable {

    private static final Vector2 dummyVector = new Vector2();

    private PhysixSystem system;
    private Entity entity;
    private Body body;

    public void init(BodyDef bodyDef, PhysixSystem system, Entity entity) {
        this.system = system;

        body = system.getWorld().createBody(bodyDef);
        body.setUserData(this);
        this.entity = entity;
    }

    @Override
    public void reset() {
        if (body != null) {
            system.getWorld().destroyBody(body);
            body = null;
        }
        system = null;
        entity = null;
    }

    public Fixture createFixture(PhysixFixtureDef fixtureDef) {
        return body.createFixture(fixtureDef);
    }

    public Body getBody() {
        return body;
    }
    
    public void setUserData(Object userData){
    	this.body.setUserData(userData);
    }
    
    public Object getUserData(){
    	return this.body.getUserData();
    }
    
    public void setBodyType(BodyType bodytype){
    	this.body.setType(bodytype);
    }
    
    public BodyType getBodyType(){
    	return this.body.getType();
    }

    public void setFixedRotation(boolean value){
    	this.body.setFixedRotation(value);
    }
    /**
     * Don't change the List !
     */
    public Array<Fixture> getFixtureList() {
        return body.getFixtureList();
    }
    
    public Fixture getFixtureByUserData(Object userData) {
        for(Fixture fixture: body.getFixtureList()) {
            if(userData.equals(fixture.getUserData())) {
                return fixture;
            }
        }
        return null;
    }

    public Entity getEntity() {
        return entity;
    }

    public float getX() {
        return system.toWorld(body.getPosition().x);
    }

    public float getY() {
        return system.toWorld(body.getPosition().y);
    }

    public Vector2 getPosition() {
        return system.toWorld(body.getPosition(), dummyVector);
    }

    public Vector2 getPosition(Vector2 destination) {
        return system.toWorld(body.getPosition(), destination);
    }

    public void setX(float x) {
        body.setTransform(dummyVector.set(system.toBox2D(x), body.getPosition().y), body.getAngle());
    }

    public void setY(float y) {
        body.setTransform(dummyVector.set(body.getPosition().x, system.toBox2D(y)), body.getAngle());
    }

    public void setPosition(Vector2 pos) {
        body.setTransform(system.toBox2D(pos, dummyVector), body.getAngle());
    }

    public void setPosition(float x, float y) {
        body.setTransform(system.toBox2D(x, y, dummyVector), body.getAngle());
    }

    public void setTransform(float x, float y, float omega) {
        body.setTransform(system.toBox2D(x, y, dummyVector), omega);
    }

    public Vector2 getLocalCenter(Vector2 origin) {
        return system.toBox2D(body.getLocalCenter(), origin);
    }

    public void simpleForceApply(Vector2 force) {
        body.applyForceToCenter(force, true);
    }

    public void applyImpulse(Vector2 speed) {
        body.applyLinearImpulse(system.toBox2D(speed, dummyVector), body.getWorldCenter(), true);
    }

    public void applyImpulse(float x, float y) {
        body.applyLinearImpulse(system.toBox2D(x, y, dummyVector), body.getWorldCenter(), true);
    }

    public float getAngle() {
        return body.getAngle();
    }

    public Vector2 getLinearVelocity() {
        return system.toWorld(body.getLinearVelocity(), dummyVector);
    }

    public void setLinearVelocity(Vector2 v) {
        body.setLinearVelocity(system.toBox2D(v, dummyVector));
    }

    public void setLinearVelocity(float x, float y) {
        body.setLinearVelocity(dummyVector.set(system.toBox2D(x), system.toBox2D(y)));
        body.setAwake(true);
    }

    public void setLinearVelocityX(float x) {
        body.setLinearVelocity(dummyVector.set(system.toBox2D(x), body.getLinearVelocity().y));
        body.setAwake(true);
    }

    public void setLinearVelocityY(float y) {
        body.setLinearVelocity(dummyVector.set(body.getLinearVelocity().x, system.toBox2D(y)));
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

    public void setAngle(float angle) {
        Vector2 pos = body.getPosition();
        body.setTransform(pos.x, pos.y, angle);
    }
}
