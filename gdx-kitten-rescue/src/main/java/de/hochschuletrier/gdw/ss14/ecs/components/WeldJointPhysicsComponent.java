package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

public class WeldJointPhysicsComponent extends PhysicsComponent{
    
    Body m_bodyA;
    Body m_bodyB;
    WeldJointDef jointDef;
    
    public WeldJointPhysicsComponent(Body bodyA, Body bodyB){
        m_bodyA = bodyA;
        m_bodyB = bodyB;
        jointDef = new WeldJointDef();
    }
    
    public void initPhysics(PhysixManager manager) {
        jointDef.bodyA = m_bodyA;
        jointDef.bodyB = m_bodyB;
        jointDef.collideConnected = false;
        manager.getWorld().createJoint(jointDef);
    }

}
