package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */

// use this class as parent for other physicsComponents (e.g. CatPhysicsComponent, DogPhysicsComponent, ...)
public class PhysicsComponent extends PhysixEntity implements Component
{
    @Override
    public void initPhysics(PhysixManager manager)
    {
        // initialize bodies and fixtures here
        // don't forget to use setPhysicsBody!
    }
}
