package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.gdx.utils.BodyEditorLoader;

public class Vase extends PhysixEntity {

	private BodyEditorLoader loader = new BodyEditorLoader(
			Gdx.files.internal("data/json/bodies.json"));
	private Vector2 position;
	private float rotation = 160f;

	public Vase(float x, float y) {
		position = new Vector2(x, y);

	}

	public void initPhysics(PhysixManager manager) {
		PhysixBody body = new PhysixBodyDef(BodyType.DynamicBody, manager)
				.position(position).fixedRotation(false).create();
		loader.attachFixture(body.getBody(), "test01", 4f);
		body.getBody().setTransform(position.x, position.y, MathUtils.degRad * rotation);
		setPhysicsBody(body);
	}

	public void setPosition(Vector2 position) {
		physicsBody.setPosition(position.x, position.y);
		physicsBody.getBody().setAwake(true);
	}

}
