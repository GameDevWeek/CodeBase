package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.Game;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends MyBaseGameState implements InputProcessor {

    private Game game;
    private Sound helicopter;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    private final SoundEmitter emitter = new SoundEmitter();
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private final Vector2 position = new Vector2(100, 100);
    private float totalMapWidth, totalMapHeight;

    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        helicopter = assetManager.getSound("helicopter");
        game = new Game();
        game.init(assetManager);
        Main.inputMultiplexer.addProcessor(this);
    }

    @Override
    public void render() {
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
    public void onEnter(MyBaseGameState previousState) {
        emitter.dispose();
        emitter.play(helicopter, true);
    }

    @Override
    public void onLeave(MyBaseGameState nextState) {
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
        return false;
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
