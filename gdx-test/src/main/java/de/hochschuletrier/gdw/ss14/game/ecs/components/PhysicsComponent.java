package de.hochschuletrier.gdw.ss14.game.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.*;

/**
 * Created by Dani on 29.09.2014.
 */

// use this class as parent for other physicsComponents (e.g. CatPhysicsComponent, DogPhysicsComponent, ...)
public class PhysicsComponent /*extends PhysixEntity*/ implements Component
{
    /*public PhysixBody physicsBody;

    @Override
    public void initPhysics(PhysixManager manager)
    {
    	
    }*/
    
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    
    public Vector2 getPosition() {
        return position;
    }
    
    public Vector2 getVelocity() {
        return velocity;
    }
}
