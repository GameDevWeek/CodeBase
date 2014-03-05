package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.devcon.CCmdFlags;
import de.hochschuletrier.gdw.commons.devcon.ConsoleCmd;
import static de.hochschuletrier.gdw.commons.devcon.DevConsole.logger;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.FontX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.game.Game;
import de.hochschuletrier.gdw.commons.gdx.devcon.DeveloperInputManager;
import de.hochschuletrier.gdw.ws1314.Main;
import java.util.List;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class GameplayState extends GameState implements InputProcessor {

    private Game game;
    private Sound click;
    private ImageX crosshair;
    private FontX verdana_24;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        crosshair = assetManager.getImageX("crosshair");
        click = assetManager.getSound("click");
        verdana_24 = assetManager.getFontX("verdana_24");
        game = new Game();
    }

    @Override
    public void render() {
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);

        game.render();

        crosshair.draw(cursor.x - crosshair.getWidth() * 0.5f, cursor.y - crosshair.getHeight() * 0.5f);

        verdana_24.drawRight(String.format("%.2f FPS", fpsCalc.getFps()), Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - verdana_24.getLineHeight());
    }

    @Override
    public void update(float delta) {
        game.update(delta);
        fpsCalc.addFrame();
    }

    @Override
    public void onEnter() {
        DeveloperInputManager.setInputProcessor(this);
    }

    @Override
    public void onLeave() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.addBall(screenX, screenY);
        click.play();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
