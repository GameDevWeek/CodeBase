package de.hochschuletrier.gdw.ss14.sandbox.Test;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;

/**
 * @author oliver
 * 
 */
public interface ICollisionListener {

    public void fireCollision(PhysixContact contact);
    
}
