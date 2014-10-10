package de.hochschuletrier.gdw.commons.gdx.state;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.ScreenCamera;
import de.hochschuletrier.gdw.commons.gdx.state.transition.Transition;

import java.util.ArrayList;

/**
 * Handles game states and their transitions
 *
 * @author Santo Pfingsten
 */
public abstract class StateBasedGame<T extends BaseGameState> implements ApplicationListener {

    private T currentState, nextState, prevState;
    private Transition entering, leaving;
    private long lastTime = System.currentTimeMillis();
    private final ArrayList<ScreenListener> screenListeners = new ArrayList();
    public final ScreenCamera screenCamera = new ScreenCamera();

    public StateBasedGame(T initialState) {
        currentState = initialState;
    }

    public T getCurrentState() {
        return currentState;
    }

    public void changeState(T state) {
        changeState(state, null, null);
    }

    public void changeState(T state, Transition out, Transition in) {
        if (state == null) {
            throw new IllegalArgumentException("State must not be null!");
        }
        if (out == null) {
            out = new Transition();
        }
        if (in == null) {
            in = new Transition();
        }
        leaving = out;
        entering = in;

        nextState = state;

        currentState.onLeave(nextState);
        nextState.onEnter(currentState);
    }

    public void addScreenListener(ScreenListener listener) {
        screenListeners.add(listener);
    }

    public void removeScreenListener(ScreenListener listener) {
        screenListeners.remove(listener);
    }

    @Override
    public void resize(int width, int height) {
        for (ScreenListener listener : screenListeners) {
            listener.resize(width, height);
        }
        screenCamera.resize(width, height);
    }

    @Override
    public final void render() {
        long time = System.currentTimeMillis();
        float delta = (time - lastTime) * 0.001f;
        lastTime = time;

        preUpdate(delta);

        updateTransitions(delta);
        
        if (leaving != null) {
            if (leaving.isReverse()) {
                leaving.render(delta, screenCamera, nextState, currentState);
            } else {
                leaving.render(delta, screenCamera, currentState, nextState);
            }
        } else if (entering != null) {
            if (entering.isReverse()) {
                entering.render(delta, screenCamera, prevState, currentState);
            } else {
                entering.render(delta, screenCamera, currentState, prevState);
            }
        } else {
            currentState.update(delta);
        }

        postUpdate(delta);
    }

    private void updateTransitions(float delta) {
        if (leaving != null) {
            leaving.update(delta);
            if (!leaving.isDone()) {
                return;
            }
            currentState.onLeaveComplete();
            prevState = currentState;
            currentState = nextState;
            nextState = null;
            leaving = null;
        }
        if (entering != null) {
            entering.update(delta);
            if (!entering.isDone()) {
                return;
            }
            currentState.onEnterComplete();
            prevState = null;
            entering = null;
        }
    }

    protected void preUpdate(float delta) {
    }

    protected void postUpdate(float delta) {
    }
}
