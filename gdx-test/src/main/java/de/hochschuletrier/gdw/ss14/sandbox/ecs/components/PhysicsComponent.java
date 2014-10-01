package de.hochschuletrier.gdw.ss14.sandbox.ecs.components;

import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Dani on 29.09.2014.
 */

// use this class as parent for other physicsComponents (e.g. CatPhysicsComponent, DogPhysicsComponent, ...)
public class PhysicsComponent extends PhysixEntity implements Component
{
    @Override
    public void initPhysics(PhysixManager manager)
    {

    }
}
