package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.AbstractPhysixComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.utils.Timer;

/**
 *
 * @author Santo Pfingsten
 */
public class Player extends AbstractPhysixComponent {

    private final Timer lastJump = new Timer();
    private final Vector2 origin = new Vector2();
    private int groundContacts;
    private Fixture feet;

    public Player(float x, float y) {
        origin.set(x, y);
    }

    public void initPhysics(PhysixManager manager) {
//        PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
//                .position(origin).fixedRotation(true).create();
//        
//        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(manager).density(1).friction(0);
//        float length = 100;
//        float radius = 30;
//        Vector2 center = new Vector2(0, -(length + radius) * 0.5f);
//        fixtureDef.shapeBox(radius * 2, length, center, 0);
//        body.createFixture(fixtureDef);
//
//        Vector2 pos = new Vector2(center.x, center.y - length * 0.5f);
//        body.createFixture(fixtureDef.shapeCircle(radius, pos));
//        pos.y += length;
//        fixtureDef.friction(0.5f);
//        feet = body.createFixture(fixtureDef.shapeCircle(radius, pos));
//        pos.y += radius * 0.6f;
//        fixtureDef.sensor(true);
//        body.createFixture(fixtureDef.shapeCircle(radius*0.8f, pos)).setUserData("ground");
//        
//        setPhysicsBody(body);
//        lastJump.reset();
    }
    
    protected void beginContact(PhysixContact contact) {
        if("ground".equals(contact.getMyFixture().getUserData())) {
            groundContacts++;
        }
    }

    protected void endContact(PhysixContact contact) {
        if("ground".equals(contact.getMyFixture().getUserData())) {
            groundContacts--;
            assert(groundContacts >= 0);
        }
    }
    
    public void update(float delta) {
        float vel=  0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            vel -= Physics.g_playerSpeed.get();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            vel += Physics.g_playerSpeed.get();
        
        if(groundContacts > 0 && Gdx.input.isKeyPressed(Input.Keys.SPACE) && lastJump.get() > 100 ) {
            lastJump.reset();
            physicsBody.setLinearVelocityY(0);
            physicsBody.applyImpulse(0, -3000);
        }
        
        if(vel != 0) {
            physicsBody.setLinearVelocityX(vel);
        }
    }
}
