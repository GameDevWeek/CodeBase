package de.hochschuletrier.gdw.ss14.ecs;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;

/**
 * @author oliver
 * 
 * To be implemented by Systems, to be able to handle collisions from entities
 * 
 */
public interface ICollisionListener {

    public void fireCollision(PhysixContact contact);
    
}
