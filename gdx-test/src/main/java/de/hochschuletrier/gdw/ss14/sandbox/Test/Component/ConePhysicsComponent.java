package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.utils.Point;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;

public class ConePhysicsComponent extends PhysicsComponent{
    
    public Vector2      mPosition;
    public float        mRadius;
    public float        mRotation;
    public float        mCorner;
    public ArrayList<Point>  mShape;
    
    /**
     * 
     * @param position      central position of the object
     * @param radius        the radius of the cone
     * @param corner        the corner of the cone (width) in radians [0 .. 2*PI]
     * @param rotation      the rotation in radians [0 .. 2*PI]
     * 
     **/
    
    public ConePhysicsComponent(Vector2 position, float radius, float rotation, float corner){
        mPosition = position;
        mRadius = radius;
        mCorner = corner;
        mRotation = rotation;
        
        
    }
    
    private void fillShapeList(){
        mShape = new ArrayList<Point>();
        float anzPunkte = (float)((mCorner/Math.toRadians(20))+1.0);
        Point firstPoint = new Point((int)(mPosition.x ), (int)(mPosition.y));
        
        mShape.add(new Point((int)mPosition.x, (int)mPosition.y));
        
        for(int i = 0; i < anzPunkte; i++){
            mShape.add(new Point((int)(mPosition.x ), (int)mPosition.y));
        }
    }
    
    public void initPhysics(PhysixManager manager){
        fillShapeList();
        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1).sensor(true);
        
        physicsBody = new PhysixBodyDef(BodyType.DynamicBody, manager).position(mPosition)
                .fixedRotation(true).angle(mRotation)
                .create();
        
    
        physicsBody.createFixture(fixturedef.shapePolygon(mShape));
        setPhysicsBody(physicsBody);
    }

}
