package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sandbox state
 * 
 * @author Santo Pfingsten
 */
public class SandboxState extends BaseGameState implements InputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SandboxState.class);

    private final SandboxGame game;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private final BitmapFont font;

    public SandboxState(AssetManagerX assetManager, SandboxGame game) {
        font = assetManager.getFont("verdana", 32);
        this.game = game;
    }

    @Override
    public void update(float delta) {
        fpsCalc.addFrame();
        
        Main.getInstance().screenCamera.bind();

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLUE);

        game.update(delta);
        Main.getInstance().screenCamera.bind();
        font.draw(DrawUtil.batch, String.format("%.2f FPS", fpsCalc.getFps()), 0, 0);
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        logger.info("entering sandbox");
        Main.inputMultiplexer.addProcessor(this);
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        logger.info("leaving sandbox");
        Main.inputMultiplexer.removeProcessor(this);
    }

    @Override
    public void dispose() {
        game.dispose();
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
}
