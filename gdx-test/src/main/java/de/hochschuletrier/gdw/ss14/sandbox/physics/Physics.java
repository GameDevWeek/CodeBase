package de.hochschuletrier.gdw.ss14.sandbox.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.AbstractPhysixComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class Physics extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(Physics.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 12;
    public static final int BOX2D_SCALE = 40;

    PhysixSystem manager = new PhysixSystem(BOX2D_SCALE, STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    private final ArrayList<AbstractPhysixComponent> entities = new ArrayList();
    private Player player;
    private final SoundEmitter emitter = new SoundEmitter();
    private Sound click;
    
    public static final CVarFloat g_playerSpeed = new CVarFloat("g_playerSpeed", 140, 0, 10000, 0, "player movement speed");
    public static final CVarFloat g_playerSpeedChange = new CVarFloat("g_playerSpeedChange", 1000, 0, 10000, 0, "player movement speed change per second");

    public Physics() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        click = assetManager.getSound("click");
        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager).position(410, 400)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(800, 20));

        PhysixUtil.createHollowCircle(manager, 180, 180, 150, 30, 6);
        player = new Player(410, 350);
        player.initPhysics(manager);
        entities.add(player);

        Main.getInstance().console.register(gravity_f);
        Main.getInstance().console.register(g_playerSpeed);
        Main.getInstance().console.register(g_playerSpeedChange);
    }
    
    @Override
    public void dispose() {
        Main.getInstance().console.unregister(gravity_f);
        Main.getInstance().console.unregister(g_playerSpeed);
        Main.getInstance().console.unregister(g_playerSpeedChange);
        emitter.dispose();
    }
    
    @Override
    public void render() {
//        manager.render();
    }

    @Override
    public void update(float delta) {
        manager.update(delta);
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

    public PhysixSystem getManager() {
        return manager;
    }

    ConsoleCmd gravity_f = new ConsoleCmd("gravity", 0, "Set gravity.", 2) {
        @Override
        public void showUsage() {
            showUsage("<x> <y>");
        }

        @Override
        public void execute(List<String> args) {
            try {
                float x = Float.parseFloat(args.get(1));
                float y = Float.parseFloat(args.get(2));

                manager.setGravity(x, y);
                logger.info("set gravity to ({}, {})", x, y);
            } catch (NumberFormatException e) {
                showUsage();
            }
        }
    };

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            addBall(screenX, screenY);
        }
        emitter.play(click, false);
        return true;
    }
}
