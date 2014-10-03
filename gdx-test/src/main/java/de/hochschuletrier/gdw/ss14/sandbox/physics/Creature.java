package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.*;

public class Creature extends PhysixEntity{
    private float x, y, w, h, a;
    private PhysixBody body;

    public Creature(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.a = 0;
    }

    public Creature(float x, float y, float width, float height, float angle){
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.a = angle;
    }

    public PhysixBody getBody(){
        return body;
    }

    @Override
    public void initPhysics(PhysixManager manager){
        body = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager).position(x, y).fixedRotation(true).create();
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(manager).density(1).friction(0);

        body.createFixture(fixtureDef.shapeBox(w, h-w, new Vector2(x, y), 0));
        body.createFixture(fixtureDef.shapeCircle(w*.5f, new Vector2(x, y-((h-w)*.5f))));
        body.createFixture(fixtureDef.shapeCircle(w*.5f, new Vector2(x, y+((h-w)*.5f)+10)));

        body.setTransform(x, y, a);
        setPhysicsBody(body);
    }
}
