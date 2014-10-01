package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;

public class CirclePhysicsComponent extends PhysicsComponent{
    public Vector2          mPosition;
    public float            mRadius;
    public float            mFriction;
    public float            mRotation;
    public float            mRestitution;
    public boolean          mIsSensor;
    
    /**
     * 
     * @param position      central position of the object
     * @param width         the width of the object
     * @param height        the height of the object
     * @param rotation      the rotation in radians [0 .. 2*PI]
     * @param friction      the friction of the object 
     * @param restitution   the restitution (elastitcy)
     * @param isSensor      set true if Body is Sensor
     */
    public CirclePhysicsComponent(Vector2 position, float radius, float rotation, float friciton, float restitutioin, boolean isSensor) {
        mPosition = position;
        mRadius = radius;
        mRotation = rotation;
        mFriction = friciton;
        mRestitution = restitutioin;
        mIsSensor = isSensor;
    }
    
    public CirclePhysicsComponent(){
        this(new Vector2(0,0), 100f, 0f, 1f, 0f, false);
    }
    
    @Override
    public void initPhysics(PhysixManager manager){
        
        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1)
                .friction(mFriction).sensor(mIsSensor).restitution(mRestitution);
        
        physicsBody = new PhysixBodyDef(BodyType.DynamicBody, manager).position(mPosition)
                .fixedRotation(true).angle(mRotation)
                .create();
        
    
        physicsBody.createFixture(fixturedef.shapeCircle(mRadius));
        setPhysicsBody(physicsBody);
    }
}