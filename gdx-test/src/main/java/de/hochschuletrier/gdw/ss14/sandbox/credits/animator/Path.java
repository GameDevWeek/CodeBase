package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class Path {

    public ArrayList<Destination> destinations;
    public ArrayList<AnimatorData.Path.Animation> animations;

    private static class Destination {

        public final Vector2 position;
        public final float speed;

        public Destination(float x, float y, float speed) {
            position = new Vector2(x, y);
            this.speed = speed;
        }
    }
}
