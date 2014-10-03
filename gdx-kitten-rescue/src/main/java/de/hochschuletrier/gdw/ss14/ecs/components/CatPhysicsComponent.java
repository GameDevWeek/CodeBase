package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.ICollisionListener;

import java.util.ArrayList;

public class CatPhysicsComponent extends PhysicsComponent {
    //Auch Hunde haben CatPhysics Component!
    public Vector2 position;
    public float width;
    public float height;
    public float friction;
    public float rotation;
    public float restitution;
    
    public ArrayList<ICollisionListener> collisionListeners;

    /**
     * Creating a CatPhysicsComponent
     *
     * @param position → central position of the object
     * @param width    → the width of the object ( width >= height)
     * @param height   → the height of the object (height < width)
     * @param rotation
     *            the rotation in radians [0 .. 2*PI]
     * @param friction
     *            the friction of the object [0..1][low friction is like ice - high like rubber]
     * @param restitution
     *            the restitution (elasticity) [0..1][low restitution is like rock - high like a ball]
     */
    public CatPhysicsComponent(Vector2 position, float width, float height,
            float rotation, float friction, float restitution) {
        
        if(height <= width) throw new IllegalArgumentException("cat needs to be higher than fat");
        
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.friction = friction;
        this.restitution = restitution;
        collisionListeners = new ArrayList<>();
    }

    @Override
    public void initPhysics(PhysixManager manager) {

        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1)
                .friction(friction).restitution(restitution);

        physicsBody = new PhysixBodyDef(BodyType.DynamicBody, manager)
                .position(position).fixedRotation(true).angle(rotation).create();

        physicsBody.setAngularVelocity(0);
        
        physicsBody.createFixture(fixturedef.shapeBox(width, height-width));
        physicsBody.createFixture(fixturedef.shapeCircle(0.1f)).setUserData("masscenter");
        physicsBody.createFixture(fixturedef.shapeCircle(width/2, new Vector2(0,( height-width)/2)));
        physicsBody.createFixture(fixturedef.shapeCircle(width/2, new Vector2(0,(-height+width)/2)));
        setPhysicsBody(physicsBody);

        physicsBody.setOwner(this);
    }
    
    @Override
    protected void beginContact(PhysixContact contact) {
        super.beginContact(contact);
        collisionListeners.forEach((l)->l.fireCollision(contact));
    }
}
