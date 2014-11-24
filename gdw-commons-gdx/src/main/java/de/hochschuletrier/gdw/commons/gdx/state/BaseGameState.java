package de.hochschuletrier.gdw.commons.gdx.state;

import com.badlogic.gdx.utils.Disposable;

/**
 * The most basic game state
 *
 * @author Santo Pfingsten
 */
public class BaseGameState implements Disposable {

    public void update(float delta) {
    }

    public void onEnter(BaseGameState previousState) {
    }

    public void onEnterComplete() {
    }

    public void onLeave(BaseGameState nextState) {
    }

    public void onLeaveComplete() {
    }

    public void dispose() {
    }

}
