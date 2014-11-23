package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends MyBaseGameState implements InputProcessor {

    private Music music;

    InputInterceptor inputProcessor;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);

        music = assetManager.getMusic("menu");

        music.setLooping(true);
//        music.play();

        inputProcessor = new InputInterceptor(this) {
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Keys.ESCAPE:
                        if (GameStateEnum.GAMEPLAY.isActive()) {
                            GameStateEnum.MAINMENU.activate(new SplitHorizontalTransition(500).reverse(), null);
                        } else {
                            GameStateEnum.GAMEPLAY.activate(new SplitHorizontalTransition(500), null);
                        }
                        return true;
                }
                return isActive && mainProcessor.keyUp(keycode);
            }
        };
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
    public void onEnter(MyBaseGameState previousState) {
        inputProcessor.setActive(true);
        inputProcessor.setBlocking(true);
    }

    @Override
    public void onLeave(MyBaseGameState nextState) {
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
        return false;
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
