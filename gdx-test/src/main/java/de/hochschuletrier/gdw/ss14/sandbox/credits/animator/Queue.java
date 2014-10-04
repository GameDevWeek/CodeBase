package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import java.util.ArrayList;
import java.util.Iterator;

public class Queue {

    protected final float originalStartTime;
    protected float startTime;
    protected final Path path;
    public Queue next;
    public Queue finalNext;
    protected final ArrayList<Item> todoItems;
    protected final ArrayList<Item> doneItems = new ArrayList();
    public final ArrayList<Animation> animations;

    public Queue(float time, Path path, ArrayList<Item> items, ArrayList<Animation> animations) {
        startTime = time;
        originalStartTime = time;
        this.path = path;
        todoItems = items;
        //todoItems.sort();
        this.animations = animations;
    }

    public void reset() {
        startTime = originalStartTime;
        Iterator<Item> it = doneItems.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            item.reset();
            todoItems.add(item);
            it.remove();
        }
        //todoItems.sort();
    }
}
