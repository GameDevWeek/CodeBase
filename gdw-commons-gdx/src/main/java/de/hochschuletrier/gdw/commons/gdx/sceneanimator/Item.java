package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class Item {

    protected static final Vector2 temp = new Vector2();
    protected final Vector2 position = new Vector2();

    public String group;
    protected Path<Vector2> path;
    protected float startTime;
    protected float originalStartTime;
    protected float pathTime;
    protected float animationTime;
    protected float totalAnimationTime;
    protected float angle;
    protected float opacity;
    public final ArrayList<Animation> animations = new ArrayList();

    public Item(String group, float startTime, float angle, float opacity) {
        this.group = group;
        this.startTime = startTime;
        originalStartTime = startTime;
        this.angle = angle;
        this.opacity = opacity;
    }

    public void reset(ArrayList<Animation> animations) {
        this.animations.clear();
        animations.stream().filter((anim) -> (isGroup(anim.group))).forEach((anim) -> {
            this.animations.add(anim);
        });
        startTime = originalStartTime;
        pathTime = 0;
        //Fixme: angle, opacity
    }

    public void setPosition(Vector2 pos) {
        position.set(pos);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void update(float deltaTime) {
        if (startTime > 0) {
            startTime -= deltaTime;
            if (startTime < 0) {
                deltaTime = -startTime;
                startTime = 0;
            }
        }
        if (startTime == 0 && path != null) {
            pathTime += deltaTime;
            setAngle(path.derivativeAt(temp, pathTime).angle());
            setPosition(path.valueAt(temp, pathTime));

            Iterator<Animation> it = animations.iterator();
            while (it.hasNext()) {
                Animation animation = it.next();
                if (animation.time < pathTime) {
                    startAnimation(animation);
                    it.remove();
                }
            }
        }
    }

    public abstract void render();

    public boolean isGroup(String group) {
        return this.group.equalsIgnoreCase(group);
    }

    public abstract void startAnimation(Animation animation);

    boolean isDone() {
        return path == null || pathTime > path.getTotalTime();
    }
}
