package de.hochschuletrier.gdw.commons.gdx.input;

import com.badlogic.gdx.InputProcessor;

public class InputInterceptor implements InputProcessor {

    protected final InputProcessor mainProcessor;
    protected boolean isActive;
    protected boolean isBlocking;

    public InputInterceptor(InputProcessor mainProcessor) {
        this.mainProcessor = mainProcessor;
    }

    public final void setActive(boolean active) {
        isActive = active;
    }

    public final boolean isActive() {
        return isActive;
    }

    public final void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    public final boolean isBlocking() {
        return isBlocking;
    }

    @Override
    public boolean keyDown(int keycode) {
        return (isActive && mainProcessor.keyDown(keycode)) || isBlocking;
    }

    @Override
    public boolean keyUp(int keycode) {
        return (isActive && mainProcessor.keyUp(keycode)) || isBlocking;
    }

    @Override
    public boolean keyTyped(char character) {
        return (isActive && mainProcessor.keyTyped(character)) || isBlocking;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return (isActive && mainProcessor.touchDown(screenX, screenY, pointer, button)) || isBlocking;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return (isActive && mainProcessor.touchUp(screenX, screenY, pointer, button)) || isBlocking;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return (isActive && mainProcessor.touchDragged(screenX, screenY, pointer)) || isBlocking;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return (isActive && mainProcessor.mouseMoved(screenX, screenY)) || isBlocking;
    }

    @Override
    public boolean scrolled(int amount) {
        return (isActive && mainProcessor.scrolled(amount)) || isBlocking;
    }
}
