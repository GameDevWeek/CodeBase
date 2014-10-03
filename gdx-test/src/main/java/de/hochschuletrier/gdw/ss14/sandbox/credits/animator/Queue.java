package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import java.util.ArrayList;
import java.util.List;

public class Queue {

    protected int time;
    protected final Path path;
    protected Queue next;
    protected Queue finalNext;
    protected final ArrayList<Item> items = new ArrayList();

    public Queue(int time, Path path) {
        this.path = path;
        this.time = time;
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
}
