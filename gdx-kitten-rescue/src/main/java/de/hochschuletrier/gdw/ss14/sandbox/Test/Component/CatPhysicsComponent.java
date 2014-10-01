package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ICollisionListener;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;

/**
 * Add System to the Listeners in Entity Factory
 * @author oliver
 *
 */
public class CatPhysicsComponent extends PhysicsComponent {

    // TODO: Für Physik Team

    public Vector2 mPosition;
    public float mWidth;
    public float mHeight;
    public float mFriction;
    public float mRotation;
    public float mRestitution;
    
    public ArrayList<ICollisionListener> mListeners;

    /**
     * 
     * @param position → central position of the object
     * @param width    → the width of the object ( width >= heigth)
     * @param height   → the height of the object (height < width)
     * @param rotation
     *            the rotation in radians [0 .. 2*PI]
     * @param friction
     *            the friction of the object
     * @param restitution
     *            the restitution (elastitcy)
     */
    public CatPhysicsComponent(Vector2 position, float width, float height,
            float rotation, float friciton, float restitutioin) {
        
        if(height <= width) throw new IllegalArgumentException("cat needs to be higher than fat");
        mListeners = new ArrayList<ICollisionListener>();
        mPosition = position;
        mWidth = width;
        mHeight = height;
        mRotation = rotation;
        mFriction = friciton;
        mRestitution = restitutioin;

    }

    public CatPhysicsComponent() {
        this(new Vector2(0, 0), 50f, 100f, 0f, 1f, 0f);
    }

    @Override
    public void initPhysics(PhysixManager manager) {

        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1)
                .friction(mFriction).restitution(mRestitution);

        physicsBody = new PhysixBodyDef(BodyType.DynamicBody, manager)
                .position(mPosition).fixedRotation(true).angle(mRotation)
                .create();

        physicsBody.setAngularVelocity(0);
        
        physicsBody.createFixture(fixturedef.shapeBox(mWidth, mHeight-mWidth));
        physicsBody.createFixture(fixturedef.shapeCircle(mWidth/2, new Vector2(0,( mHeight - mWidth)/2)));
        physicsBody.createFixture(fixturedef.shapeCircle(mWidth/2, new Vector2(0,(-mHeight + mWidth)/2)));
        setPhysicsBody(physicsBody);
        
    }
    
    @Override
    protected void beginContact(PhysixContact contact) {
        super.beginContact(contact);
        mListeners.forEach((l)->l.fireCollision(contact));
    }
}
