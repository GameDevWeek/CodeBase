package de.hochschuletrier.gdw.ss14.physics;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;

/**
 * @author oliver
 * 
 * To be implemented by Systems, to be able to handle collisions from entities
 * 
 */
public interface ICollisionListener {

    public void fireBeginnCollision(PhysixContact contact);
    public void fireEndCollision(PhysixContact contact);
    
}
