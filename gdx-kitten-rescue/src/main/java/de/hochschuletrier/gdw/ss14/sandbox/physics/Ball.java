package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.utils.Timer;

/**
 *
 * @author Santo Pfingsten
 */
public class Ball extends PhysixEntity {

    private final Vector2 origin = new Vector2();
    private final float radius;
    private final Sound touchSound;
    private final Timer lastSound = new Timer();

    public Ball(float x, float y, float radius, Sound touchSound) {
        origin.set(x, y);
        this.radius = radius;
        this.touchSound = touchSound;
        lastSound.reset();
    }

    public void initPhysics(PhysixManager manager) {
        PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager).position(origin)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(5).friction(0.2f).restitution(0.4f).shapeCircle(radius));
        setPhysicsBody(body);
    }
    
    protected void preSolve(PhysixContact contact, Manifold oldManifold) {
    }
    protected void postSolve(PhysixContact contact, ContactImpulse impulse) {
        if(lastSound.get() > 100) {
            float impulseStrength = Math.abs(impulse.getNormalImpulses()[0]);
            float speed = contact.getMyPhysixBody().getLinearVelocity().len();
            if(impulseStrength > 20 && speed > 20) {
                lastSound.reset();
                SoundEmitter.playGlobal(touchSound, false, physicsBody.getX(), physicsBody.getY(), 0);
            }
        }
    }
    
    protected void beginContact(PhysixContact contact) {
    }
}
