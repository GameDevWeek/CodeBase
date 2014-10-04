package de.hochschuletrier.gdw.ss14.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;

public class SlideMass extends PhysicsComponent{
    
    public Body m_bodyA;
    public Body m_bodyB;
    WeldJointDef jointDef;
    
    public SlideMass(Body bodyA){
        m_bodyA = bodyA;
    }
    
    public void init(PhysixManager manager){
        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1).sensor(true);
        
        PhysixBody pb = new PhysixBodyDef(BodyType.DynamicBody, manager)
            .position(m_bodyA.getLocalCenter()).fixedRotation(true).angle(m_bodyA.getAngle()).create();
                
        pb.createFixture(fixturedef.shapeCircle(30));
        m_bodyB = pb.getBody();
        jointDef.bodyA = m_bodyA;
        jointDef.bodyB = m_bodyB;
        jointDef.collideConnected = false;
        manager.getWorld().createJoint(jointDef);
    }

}
