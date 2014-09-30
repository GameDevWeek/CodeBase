package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;

public class CatPhysicsComponent extends PhysicsComponent{
	
	// TODO: Für Physik Team
	
	public Vector2 			mPosition;
	public float 			mWidth;
	public float 			mHeight;
	public float 			mFriction;
	public float 			mRotation;
	public float			mRestitution;
	

	/**
	 * 
	 * @param position 		central position of the object
	 * @param width			the width of the object
	 * @param height		the height of the object
	 * @param rotation		the rotation in radians [0 .. 2*PI]
	 * @param friction  	the friction of the object 
	 * @param restitution	the restitution (elastitcy)
	 */
	public CatPhysicsComponent(Vector2 position, float width, float height, float rotation, float friciton, float restitutioin) {
		mPosition = position;
		mWidth = width;
		mHeight = height;
		mRotation = rotation;
		mFriction = friciton;
		mRestitution = restitutioin;
	}
	
	public CatPhysicsComponent(){
		this(new Vector2(0,0), 100f, 100f, 0f, 1f, 0f);
	}
	
	@Override
    public void initPhysics(PhysixManager manager){
		PhysixFixtureDef fixturedef = new PhysixFixtureDef(manager).density(1)
				.friction(mFriction).restitution(mRestitution);
		
		physicsBody = new PhysixBodyDef(BodyType.DynamicBody, manager).position(mPosition)
				.fixedRotation(true).angle(mRotation)
				.create();
		

	
		physicsBody.createFixture(fixturedef.shapeBox(mWidth, mHeight));
		setPhysicsBody(physicsBody);
	}

	
}
