package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends BaseGameState implements InputProcessor {

    private final AssetManagerX assetManager;
    private final Music music;

    InputInterceptor inputProcessor;

    public MainMenuState(AssetManagerX assetManager) {
        this.assetManager = assetManager;
        music = assetManager.getMusic("menu");

        music.setLooping(true);
//        music.play();

        inputProcessor = new InputInterceptor(this);
        Main.inputMultiplexer.addProcessor(inputProcessor);
    }

    public void render() {
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.GRAY);
    }

    @Override
    public void update(float delta) {
        render();
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(true);
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        inputProcessor.setActive(false);
        inputProcessor.setBlocking(false);
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
        Main main = Main.getInstance();
        if(!main.isTransitioning()) {
            main.changeState(new GameplayState(assetManager), new SplitHorizontalTransition(500), null);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
