package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */
public class GroundPhysicsComponent extends PhysicsComponent
{
    
    private PhysixBodyDef bodydef;
    private PhysixFixtureDef fixturedef;
    
    public GroundPhysicsComponent(PhysixBodyDef bodydef, PhysixFixtureDef fixturedef) {
        this.bodydef = bodydef;
        this.fixturedef = fixturedef;
    }
    
    @Override
    public void initPhysics(PhysixManager manager) {
        super.initPhysics(manager);
        
        physicsBody = bodydef.create();
        
        physicsBody.createFixture(fixturedef);
        
        setPhysicsBody(physicsBody);
    }
}
