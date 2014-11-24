package de.hochschuletrier.gdw.ss14.sandbox;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

public abstract class SandboxGame implements InputProcessor, Disposable {

    public abstract void init(AssetManagerX assetManager);

    public abstract void update(float delta);

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
