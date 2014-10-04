package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Iterator;

public class Queue {

    protected final float originalStartTime;
    protected float startTime;
    protected float stateTime;
    protected final Path<Vector2> path;
    public Queue next;
    public Queue finalNext;
    protected final ArrayList<Item> items;
    public final ArrayList<Animation> animationsTodo;
    public final ArrayList<Animation> animationsDone = new ArrayList();

    public Queue(float time, Path<Vector2> path, ArrayList<Item> items, ArrayList<Animation> animations) {
        startTime = time;
        originalStartTime = time;
        this.path = path;
        this.items = items;
        //todoItems.sort();
        this.animationsTodo = animations;
    }

    public void reset() {
        startTime = originalStartTime;
        for(Item item: items) {
            item.reset();
        }
        //todoItems.sort();
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
            stateTime += delta;
            Iterator<Animation> it = animationsTodo.iterator();
            while(it.hasNext()) {
                Animation animation = it.next();
                if(animation.time < stateTime) {
                    for(Item item: items) {
                        item.startAnimation(animation);
                    }
                    animationsDone.add(animation);
                    it.remove();
                }
            }
            // update items
            for(Item item: items) {
                item.update(path, delta);
            }
        }
    }
}
