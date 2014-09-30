package de.hochschuletrier.gdw.ss14.sandbox.ecs.components;

import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class PhysicsComponent extends PhysixEntity implements Component
{
    public PhysixBody physicsBody;

    @Override
    public void initPhysics(PhysixManager manager)
    {
    	
    }
}
