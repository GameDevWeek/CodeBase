package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.devcon.cvar.ICVarListener;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.DefaultOrthoCameraController;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.game.Game;

/**
 * Menu state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends GameState implements InputProcessor, ScreenListener, ICVarListener {

    private Game game;
    private Sound click, helicopter;
    private Texture crosshair;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    private final SoundEmitter emitter = new SoundEmitter();
    private final CVarEnum<SoundEmitter.Mode> mode = new CVarEnum<SoundEmitter.Mode>("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");

    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        crosshair = assetManager.getTexture("crosshair");
        click = assetManager.getSound("click");
        helicopter = assetManager.getSound("helicopter");
        game = new Game();
        Main.inputMultiplexer.addProcessor(this);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Main.getInstance().addScreenListener(this);
        Main.getInstance().console.register(mode);
        mode.addListener(this);
    }

    @Override
    public void resize(int width, int height) {
        SoundEmitter.setListenerPosition(width / 2, height / 2, 10, mode.get());
    }

    @Override
    public void modified(CVar cvar) {
        if (cvar == mode) {
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void render() {
        DrawUtil.batch.setProjectionMatrix(DrawUtil.getCamera().combined);

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                Color.BLUE);

        game.render();
    }

    @Override
    public void update(float delta) {
        emitter.update();
        emitter.setPosition(cursor.x, cursor.y, 0);
        game.update(delta);

        fpsCalc.addFrame();
    }

    @Override
    public void onEnter() {
        emitter.dispose();
        emitter.play(helicopter, true);
    }

    @Override
    public void onLeave() {
        emitter.dispose();
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
        if (button == 0) {
            game.addBall(screenX, screenY);
        }
//		else
//			game.getVase().setPosition(new Vector2(screenX, screenY));
        emitter.play(click, false);
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
