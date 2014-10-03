package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 *
 * @author Santo Pfingsten
 */
public class Physics extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(Physics.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    PhysixManager manager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);
    private final ArrayList<PhysixEntity> entities = new ArrayList<>();
    private Player player;
    private Sound click;
    
    public static final CVarFloat g_playerSpeed = new CVarFloat("g_playerSpeed", 140, 0, 10000, 0, "player movement speed");
    public static final CVarFloat g_playerSpeedChange = new CVarFloat("g_playerSpeedChange", 1000, 0, 10000, 0, "player movement speed change per second");

    public Physics() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        click = assetManager.getSound("click");
        PhysixBody bodyA = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager).position(410, 400).fixedRotation(false).create();
        bodyA.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(200, 20));

        PhysixUtil.createHollowCircle(manager, 180, 180, 150, 30, 6);
        player = new Player(300, 100);
        player.initPhysics(manager);
        entities.add(player);

        Main.getInstance().console.register(g_playerSpeed);
        Main.getInstance().console.register(g_playerSpeedChange);
    }
    
    @Override
    public void dispose() {
        Main.getInstance().console.unregister(g_playerSpeed);
        Main.getInstance().console.unregister(g_playerSpeedChange);
    }
    
    @Override
    public void render() {
        manager.render();
    }

    @Override
    public void update(float delta) {
        manager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        player.update(delta);
    }

    public void addBall(int x, int y) {
        Ball b = new Ball(x, y, 30, click);
        b.initPhysics(manager);
        entities.add(b);
    }

    public Player getPlayer() {
        return player;
    }

    public PhysixManager getManager() {
        return manager;
    }
}
