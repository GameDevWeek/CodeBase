package de.hochschuletrier.gdw.ss14.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

public class RayCastPhysics implements RayCastCallback{
    
    public Fixture m_fixture;
    public boolean m_hit;
    public Vector2 m_point;
    public Vector2 m_normal;
    public float m_fraction;
    
    public RayCastPhysics()
    {
            reset();
    }
    
    public void reset(){
        m_hit = false;
        m_point = null;
        m_normal = null;
        m_fraction = 0;
    }

    public float reportRayFixture(Fixture fixture,  Vector2 point,
             Vector2 normal, float fraction)
    {
            Body body = fixture.getBody();
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
            return 0f;
    }
}
