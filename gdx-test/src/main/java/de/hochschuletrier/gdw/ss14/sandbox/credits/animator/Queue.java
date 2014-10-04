package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;


import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Iterator;

public class Queue {

    protected final float originalStartTime;
    protected float startTime;
    protected final Path<Vector2> path;
    public Queue next;
    public Queue finalNext;
    protected final ArrayList<Item> items;
    public final ArrayList<Animation> animations;

    public Queue(float time, Path<Vector2> path, ArrayList<Item> items, ArrayList<Animation> animations) {
        startTime = time;
        originalStartTime = time;
        this.path = path;
        this.items = items;
        this.animations = animations;
        reset();
    }

    public void reset() {
        startTime = originalStartTime;
        for(Item item: items) {
            item.reset(animations);
        }
    }
    
    public void start() {
        startTime = 0;
    }
    
    public void render() {
        if(startTime == 0) {
            // render items
            for(Item item: items) {
                item.render();
            }
        }
    }
    
    public void update(float delta) {
        if(startTime > 0) {
            startTime -= delta;
            if(startTime > 0) {
                return;
            } else if(startTime < 0) {
                delta = -startTime;
                startTime = 0;
            }
        }
        if(startTime == 0) {
            // update items
            for(Item item: items) {
                item.update(path, delta);
            }
        }
    }

    boolean isDone() {
        boolean done = false;
        if(startTime == 0) {
            done = true;
            // update items
            for(Item item: items) {
                if(!item.isDone(path)) {
                    done = false;
                    break;
                }
            }
        }
        return done;
    }
}
