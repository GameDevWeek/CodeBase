package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */
public class PuddlePhysicsComponent extends PhysicsComponent
{
    
    
    
    public PuddlePhysicsComponent(PhysixBody body) {
        physicsBody = body;
    }
    
    @Override
    public void initPhysics(PhysixManager manager) {
        super.initPhysics(manager);
        
        setPhysicsBody(physicsBody);
    }
}
