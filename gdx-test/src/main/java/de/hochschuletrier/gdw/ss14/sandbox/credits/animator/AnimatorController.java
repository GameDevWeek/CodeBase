package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.AnimatorData.Path.Animation;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimatorController {

    private final HashMap<String, TextStyle> textStyles = new HashMap();
    private final HashMap<String, Queue> queues = new HashMap();
    private final HashMap<String, Path> paths = new HashMap();

    private static class Queue {

        public Path path;
        public int time;
        public Queue next;
        public Queue finalNext;
        public ArrayList<Item> items;
    }

    private static class Path {

        public ArrayList<Destination> destinations;
        public ArrayList<Animation> animations;

        private static class Destination {

            public final Vector2 position;
            public final float speed;

            public Destination(float x, float y, float speed) {
                position = new Vector2(x, y);
                this.speed = speed;
            }
        }
    }
}
