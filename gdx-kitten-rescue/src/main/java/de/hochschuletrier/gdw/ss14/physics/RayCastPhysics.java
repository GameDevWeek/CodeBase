package de.hochschuletrier.gdw.ss14.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

import java.util.ArrayList;

public class RayCastPhysics implements RayCastCallback{
    
    public Fixture m_fixture;
    public boolean m_hit;
    public Vector2 m_point;
    public Vector2 m_normal;
    public float m_fraction;
    public ArrayList<RayCastPhysics> collisions = new ArrayList<>();
    public ArrayList<Body> collisionBodys = new ArrayList<>();
    
    /**
     * Creating a RayCastCallback
     *
     * @param m_fixture  → the fixture of the collision
     * @param m_hit      → true if the RayCast ever hit something
     * @param m_point    → the point of the initial intersection
     * @param m_normal   → the normal vector at the point of intersection
     * @param m_fraction → used to define the behavior of the RayCast after collision
     * @param collisions → Array with all collisions of the RayCast, 
     *    stored in RayCastPhysics Objects including the collisions data
     * 
     */
    
    public RayCastPhysics()
    {
        reset();
    }
    
    public void reset(){
        m_hit = false;
        m_point = null;
        m_normal = null;
        m_fraction = 0;
        collisions.clear();
    }

    public float reportRayFixture(Fixture fixture,  Vector2 point,
             Vector2 normal, float fraction)
    {
        Body body = fixture.getBody();
        collisionBodys.add(body);
        Object userData = body.getUserData();
        if (userData != null)
        {
                int index = (Integer)userData.hashCode();
                if (index == 0){
                        return -1f;
                }
        }

        m_hit = true;
        m_point = point;
        m_normal = normal;
        m_fraction = fraction;
        m_fixture = fixture;
        collisions.add(this);
        return 1f;
    }
}