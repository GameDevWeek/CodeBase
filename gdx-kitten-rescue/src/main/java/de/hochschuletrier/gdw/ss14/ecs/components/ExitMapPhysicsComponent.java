package de.hochschuletrier.gdw.ss14.ecs.components;

import java.io.File;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;

public class ExitMapPhysicsComponent extends PhysicsComponent{
    
    private Vector2 mPosition;
    private float width, height;
    
    public ExitMapPhysicsComponent(Vector2 pos, float width, float height){
        mPosition = pos;
        this.width = width;
        this.height = height;
    }

    @Override
    public void initPhysics(PhysixManager manager){
        PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1).sensor(true);
        
        physicsBody = new PhysixBodyDef(BodyType.StaticBody, manager).position(mPosition)
                .fixedRotation(false)
                .create();
        
        physicsBody.createFixture(fixturedef.shapeBox(width, height)).isSensor();
        setPhysicsBody(physicsBody);
        physicsBody.setOwner(this);
    }
    
    public Vector2 getPosition() {
        return mPosition;
    }

    public void setPosition(Vector2 mPosition) {
        this.mPosition = mPosition;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
