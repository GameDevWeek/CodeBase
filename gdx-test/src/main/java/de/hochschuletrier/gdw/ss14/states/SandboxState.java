package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
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
public class SandboxState extends MyBaseGameState implements InputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SandboxState.class);

    private SandboxGame game;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private final HashMap<String, Class> sandboxClasses = new HashMap();
    BitmapFont font;

    public SandboxState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        font = assetManager.getFont("verdana", 32);
        try {
            for (Class clazz : ClassUtils.findClassesInPackage("de.hochschuletrier.gdw.ss14.sandbox")) {
                if (!Modifier.isAbstract(clazz.getModifiers()) && SandboxGame.class.isAssignableFrom(clazz)) {
                    sandboxClasses.put(clazz.getSimpleName(), clazz);
                }
            }
        } catch (IOException ex) {
            logger.error("Can't find sandbox classes", ex);
        }

        Main.getInstance().console.register(sandbox_f);
    }

    public void render() {
        Main.getInstance().screenCamera.bind();

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLUE);

        game.render();
        Main.getInstance().screenCamera.bind();
        font.draw(DrawUtil.batch, String.format("%.2f FPS", fpsCalc.getFps()), 0, 0);
    }

    @Override
    public void update(float delta) {
        game.update(delta);

        fpsCalc.addFrame();
        render();
    }

    @Override
    public void onEnter(MyBaseGameState previousState) {
        assert (game != null);
        logger.info("entering sandbox");
        Main.inputMultiplexer.addProcessor(this);
    }

    @Override
    public void onLeave(MyBaseGameState nextState) {
        logger.info("leaving sandbox");
        Main.inputMultiplexer.removeProcessor(this);
    }

    @Override
    public void onLeaveComplete() {
        if (game != null) {
            game.stop();
            game = null;
        }
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
        public void complete(String prefix, List<String> results) {
            for (String sbc : sandboxClasses.keySet()) {
                if (sbc.startsWith(prefix)) {
                    results.add(sbc);
                }
            }
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
                        game.stop();
                    }
                    game = (SandboxGame) clazz.newInstance();
                    game.init(assetManager);
                    logger.info("starting sandbox {}", gameName);
                    GameStateEnum.SANDBOX.activate();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("Could not create instance of class", e);
                }
            }
        }
    };
}
