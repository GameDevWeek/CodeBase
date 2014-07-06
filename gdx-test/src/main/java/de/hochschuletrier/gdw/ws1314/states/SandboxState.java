package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.sandbox.SandboxGame;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sandbox state
 * 
 * @author Santo Pfingsten
 */
public class SandboxState extends GameState implements InputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SandboxState.class);

    private SandboxGame game;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private final HashMap<String, Class> sandboxClasses = new HashMap();

    public SandboxState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        try {
            for (Class clazz : ClassUtils.findClassesInPackage("de.hochschuletrier.gdw.ws1314.sandbox")) {
                if (!Modifier.isAbstract(clazz.getModifiers()) && SandboxGame.class.isAssignableFrom(clazz)) {
                    sandboxClasses.put(clazz.getSimpleName(), clazz);
                }
            }
        } catch (IOException ex) {
            logger.error("Can't find sandbox classes", ex);
        }

        Main.getInstance().console.register(sandbox_f);
    }

    @Override
    public void render() {
        DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLUE);

        game.render();
    }

    @Override
    public void update(float delta) {
        game.update(delta);

        fpsCalc.addFrame();
    }

    @Override
    public void onEnter() {
        assert (game != null);
        logger.info("entering sandbox");
        Main.inputMultiplexer.addProcessor(this);
    }

    @Override
    public void onLeave() {
        logger.info("leaving sandbox");
        Main.inputMultiplexer.removeProcessor(this);
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return game.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return game.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return game.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return game.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return game.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return game.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return game.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return game.scrolled(amount);
    }

    ConsoleCmd sandbox_f = new ConsoleCmd("sandbox", 0, "Run a sandbox game.", 1) {
        @Override
        public void showUsage() {
            showUsage("<sandbox name>");
        }

        @Override
        public void complete(String arg, List<String> results) {
            results.addAll(sandboxClasses.keySet());
        }

        @Override
        public void execute(List<String> args) {
            String gameName = args.get(1);
            Class clazz = sandboxClasses.get(gameName);
            if (clazz == null) {
                logger.warn("'{}' is not a sandbox class");
                showUsage();
            } else {
                try {
                    if (game != null) {
                        game.dispose();
                    }
                    game = (SandboxGame) clazz.newInstance();
                    game.init(assetManager);
                    logger.info("starting sandbox {}", gameName);
                    GameStates.SANDBOX.activate(500);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("Could not create instance of class", e);
                }
            }
        }
    };
}
