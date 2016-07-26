package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import java.util.ArrayList;

/**
 *
 * @author Santo Pfingsten
 */
public class Queue {

    protected final float originalStartTime;
    protected float startTime;
    public Queue next;
    public Queue finalNext;
    protected final ArrayList<Item> items;
    public final ArrayList<Animation> animations;
    public final int layer;
    private boolean done;
    private boolean lastItemStarted;

    public Queue(float time, int layer, ArrayList<Item> items, ArrayList<Animation> animations) {
        startTime = time;
        this.layer = layer;
        originalStartTime = time;
        this.items = items;
        this.animations = animations;
        reset();
    }

    public void reset() {
        startTime = originalStartTime;
        done = false;
        lastItemStarted = false;
        for (Item item : items) {
            item.reset(animations);
            if(done && !item.isDone())
                done = false;
        }
    }

    public void start() {
        startTime = 0;
        update(0);
    }

    public void render() {
        if (startTime == 0 && !done) {
            // render items
            for (Item item : items) {
                if (item.shouldRender()) {
                    item.render();
                }
            }
        }
    }

    public void update(float delta) {
        if (startTime > 0) {
            startTime -= delta;
            if (startTime > 0) {
                return;
            } else if (startTime < 0) {
                delta = -startTime;
                startTime = 0;
            }
        }
        if (startTime == 0 && !done) {
            done = true;
            boolean allStarted = true;
            // update items
            for (Item item : items) {
                item.update(delta);
                if(!item.isStarted())
                    allStarted = false;
                if(done && !item.isDone())
                    done = false;
            }
            if(!lastItemStarted) {
                if(allStarted && next != null)
                    next.start();
                lastItemStarted = true;
            }
            if(done && finalNext != null) {
                finalNext.start();
            }
        }
    }

    boolean isDone() {
        if (startTime == 0)
            return done;
        return false;
    }

    void abortPausePaths() {
        for (Item item : items) {
            item.abortPausePath();
        }
    }
}
