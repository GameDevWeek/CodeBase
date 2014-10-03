package de.hochschuletrier.gdw.ss14.sandbox.credits;

import com.badlogic.gdx.math.Vector2;
import java.util.List;

public class PathMover {

    boolean oriented = true;
    List<Destination> path;
    int destination = 0;
    Vector2 position;
    float angle;
    private int delay;
    protected Runnable doneAction;

    public PathMover(List<Destination> path, boolean oriented) {
        this.oriented = oriented;
        this.position = new Vector2(path.get(0));
        this.path = path;
        destination = 1;
        if (oriented) {
            Vector2 dir = new Vector2(path.get(1)).sub(position).nor();
            angle = (float) Math.toDegrees(Math.atan2(dir.y, dir.x)) - 90;
        }
    }

    public static class Destination extends Vector2 {

        private final float speed;
        private final int delay;

        public Destination(float x, float y, float speed, int delay) {
            super(x, y);
            this.speed = speed;
            this.delay = delay;
        }

        public float getSpeed() {
            return speed;
        }

        public int getDelay() {
            return delay;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public PathMover delay(int value) {
        delay = value;
        return this;
    }

    public void update(int delta) {
        if (delta <= 0 || destination == -1) {
            return;
        }

        if (delta > 100) {
            delta = 100;
        }

        delay -= delta;
        if (delay >= 0) {
            return;
        }

        delay = 0;

        Destination next = path.get(destination);
        float speed = next.getSpeed();
        Vector2 dir = new Vector2(next).sub(position).nor();

        boolean arrived = speed == -1;
        if (!arrived) {
            float step = speed * delta / 1000.0f;
            dir.scl(step);
            arrived = position.dst(next) <= step;
        }

        if (arrived) {
            position.set(next);
            // move done
            destination++;
            if (destination == path.size()) {
                if (doneAction != null) {
                    doneAction.run();
                }
                destination = -1;
            } else {
                next = path.get(destination);
                delay = next.getDelay();
            }
        } else {
            position.add(dir);
        }

        dir.nor();
        if (oriented) {
            angle = (float) Math.toDegrees(Math.atan2(dir.y, dir.x)) - 90;
        }
    }

    public void done(Runnable value) {
        doneAction = value;
    }
}
