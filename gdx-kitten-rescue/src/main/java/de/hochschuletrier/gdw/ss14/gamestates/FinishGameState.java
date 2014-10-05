package de.hochschuletrier.gdw.ss14.gamestates;

import com.badlogic.gdx.InputProcessor;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sound.LocalMusic;
import de.hochschuletrier.gdw.ss14.ui.FinishMenu;
import de.hochschuletrier.gdw.ss14.ui.FinishMenu.FinishState;

public class FinishGameState extends KittenGameState implements InputProcessor {

    private FinishMenu finishMenu;
    InputInterceptor inputProcessor;
    private LocalMusic music;

    @Override
    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
    }
    
    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        finishMenu.render();
    }

    @Override
    public void update(float delta) {
        finishMenu.update(delta);
    }

    @Override
    public void onEnter(KittenGameState previousState) {
        finishMenu = new FinishMenu();
        
        finishMenu.init(assetManager);
    }

    @Override
    public void onEnterComplete() {
    }

    @Override
    public void onLeave(KittenGameState nextState) {
        finishMenu.dispose();
    }

    @Override
    public void onLeaveComplete() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }
}
