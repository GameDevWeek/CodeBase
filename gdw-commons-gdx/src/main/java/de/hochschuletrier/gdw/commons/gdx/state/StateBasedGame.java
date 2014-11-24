package de.hochschuletrier.gdw.commons.gdx.state;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.Disposable;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.ScreenCamera;
import de.hochschuletrier.gdw.commons.gdx.state.transition.Transition;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles game states and their transitions
 *
 * @author Santo Pfingsten
 */
public abstract class StateBasedGame implements ApplicationListener {

    private final HashMap<Class<? extends BaseGameState>, BaseGameState> persistentStates = new HashMap();
    private BaseGameState currentState, nextState, prevState;
    private Transition entering, leaving;
    private long lastTime = System.currentTimeMillis();
    private final ArrayList<ScreenListener> screenListeners = new ArrayList();
    public final ScreenCamera screenCamera = new ScreenCamera();

    public StateBasedGame(BaseGameState initialState) {
        currentState = initialState;
    }
    
    public boolean isTransitioning() {
        return nextState != null || prevState != null;
    }

    public BaseGameState getCurrentState() {
        return currentState;
    }

    public BaseGameState getPersistentState(Class<? extends BaseGameState> clazz) {
        return persistentStates.get(clazz);
    }

    public void addPersistentState(BaseGameState state) {
        if (state == null) {
            throw new IllegalArgumentException("State must not be null!");
        }
        if(persistentStates.containsKey(state.getClass())) {
            throw new IllegalStateException("A state of that class has already been added!");
        }
        persistentStates.put(state.getClass(), state);
    }

    public void removePersistentState(Class clazz) {
        persistentStates.remove(clazz);
    }

    public void changeState(BaseGameState state) {
        changeState(state, null, null);
    }

    public void changeState(BaseGameState state, Transition out, Transition in) {
        if (state == null) {
            throw new IllegalArgumentException("State must not be null!");
        }
        
        if(isTransitioning()) {
            if (leaving != null) {
                completeLeave();
            }
            if (entering != null) {
                completeEnter();
            }
        }
        
        leaving = out;
        entering = in;

        nextState = state;

        currentState.onLeave(nextState);
        nextState.onEnter(currentState);
        
        if(leaving == null) {
            completeLeave();
        }
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
            completeLeave();
        }
        if (entering != null) {
            entering.update(delta);
            if (!entering.isDone()) {
                return;
            }
            completeEnter();
        }
    }

    private void completeLeave() {
        currentState.onLeaveComplete();
        prevState = currentState;
        currentState = nextState;
        nextState = null;
        leaving = null;
        
        if(entering == null) {
            completeEnter();
        }
    }

    private void completeEnter() {
        if(!persistentStates.containsValue(prevState)) {
            prevState.dispose();
        }
        currentState.onEnterComplete();
        prevState = null;
        entering = null;
    }
    
    @Override
    public void dispose() {
        persistentStates.values().forEach(Disposable::dispose);
    }

    protected void preUpdate(float delta) {
    }

    protected void postUpdate(float delta) {
    }
}
