package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;

public class CatPhysicsComponent extends PhysicsComponent{
	
	// TODO: Für Physik Team
	
	public CatPhysicsComponent(){
		
	}
	
	@Override
    public void initPhysics(PhysixManager manager)
    {
		super.initPhysics(manager);

        this.physicsBody = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager).position(new Vector2(0,0))
                .fixedRotation(false).create();
        physicsBody.createFixture(new PhysixFixtureDef(manager).density(5).friction(0.2f).restitution(0.4f).shapeCircle(50));
        setPhysicsBody(physicsBody);
    }

}
