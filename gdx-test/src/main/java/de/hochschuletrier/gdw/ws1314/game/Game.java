package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import static de.hochschuletrier.gdw.commons.devcon.DevConsole.logger;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.utils.PhysixUtil;
import java.util.ArrayList;
import java.util.List;

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

    private TextureAtlas atlas;
    PhysixManager manager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);
    private final ArrayList<PhysixEntity> entities = new ArrayList<PhysixEntity>();
    private final Player player;
    private final Vase vase;

    public Game() {
        atlas = new TextureAtlas("data/images/atlas.atlas");

        PhysixBody body = new PhysixBodyDef(BodyType.StaticBody, manager).position(410, 400)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(800, 20));

        PhysixUtil.createHollowCircle(manager, 180, 180, 150, 30, 6);
        player = new Player(410, 350);
        player.initPhysics(manager);
        entities.add(player);

        vase = new Vase(0, 0);
        vase.initPhysics(manager);
        vase.initGraphics(atlas);
        entities.add(vase);
        Main.getInstance().console.register(gravity_f);
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

    public PhysixManager getManager() {
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
}
