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
        for (Item item : items) {
            item.reset(animations);
        }
    }

    public void start() {
        startTime = 0;
    }

    public void render() {
        if (startTime == 0) {
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
        if (startTime == 0) {
            // update items
            for (Item item : items) {
                item.update(delta);
            }
        }
    }

    boolean isDone() {
        boolean done = false;
        if (startTime == 0) {
            done = true;
            // update items
            for (Item item : items) {
                if (!item.isDone()) {
                    done = false;
                    break;
                }
            }
        }
        return done;
    }
}
