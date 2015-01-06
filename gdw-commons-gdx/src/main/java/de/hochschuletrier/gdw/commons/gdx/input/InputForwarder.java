package de.hochschuletrier.gdw.commons.gdx.input;

import com.badlogic.gdx.InputProcessor;

public class InputForwarder implements InputProcessor {

    protected InputProcessor mainProcessor;

    public void set(InputProcessor mainProcessor) {
        this.mainProcessor = mainProcessor;
    }

    public InputProcessor get() {
        return mainProcessor;
    }

    @Override
    public boolean keyDown(int keycode) {
        return this.mainProcessor != null && mainProcessor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return this.mainProcessor != null && mainProcessor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return this.mainProcessor != null && mainProcessor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return this.mainProcessor != null && mainProcessor.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return this.mainProcessor != null && mainProcessor.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return this.mainProcessor != null && mainProcessor.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return this.mainProcessor != null && mainProcessor.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return this.mainProcessor != null && mainProcessor.scrolled(amount);
    }
}
