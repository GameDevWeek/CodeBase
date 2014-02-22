package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.utils.PhysixUtil;
import java.util.ArrayList;

/**
 *
 * @author Santo Pfingsten
 */
public class Game {

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 12;
    public static final int BOX2D_SCALE = 40;

    PhysixManager manager = new PhysixManager(BOX2D_SCALE, GRAVITY);
    private final ArrayList<PhysixEntity> entities = new ArrayList<PhysixEntity>();
    private final Player player;
	private final Vase vase;

    public Game() {
        PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager).position(410, 400)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(800, 20));

        PhysixUtil.createHollowCircle(manager, 180, 180, 150, 30, 6);
        player = new Player(410, 350);
        player.initPhysics(manager);
		entities.add(player);

		vase = new Vase(300, 300);
		vase.initPhysics(manager);
		entities.add(vase);
    }

    public void render() {
        manager.render();
    }

    public void update(float delta) {
        manager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    public void addBall(int x, int y) {
        Ball b = new Ball(x, y, 30);
        b.initPhysics(manager);
        entities.add(b);
    }

	public Player getPlayer() {
		return player;
	}

	public Vase getVase() {
		return vase;
	}

}
