package de.hochschuletrier.gdw.commons.gdx.state.transition;

import de.hochschuletrier.gdw.commons.gdx.state.GameState;

/**
 * An empty Transition and base class for other transitions
 *
 * @author Santo Pfingsten
 * @param <T> The final class
 */
public class Transition<T extends Transition> {

    private int fadeTime;
    private boolean reverse;
    private float progress = 0;

    public Transition() {
        this(0);
    }

    protected Transition(int fadeTime) {
        if (fadeTime > 0) {
            this.fadeTime = fadeTime;
        } else {
            this.fadeTime = 1;
            progress = 1;
        }
    }

    public float getProgress() {
        return reverse == false ? progress : (1.0f - progress);
    }

    public boolean isDone() {
        return (progress >= 1);
    }

    public boolean isReverse() {
        return reverse;
    }

    public T reverse() {
        reverse = !reverse;
        return (T) this;
    }

    public void render(GameState from, GameState to) {
    }

    public void update(int delta) {
        if (progress < 1) {
            if (delta > 16) {
                delta = 16;
            }

            progress += delta * (1.0f / fadeTime);
            if (progress > 1) {
                progress = 1;
            }
        }
    }
}
