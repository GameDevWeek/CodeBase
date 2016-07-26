package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import com.badlogic.gdx.audio.Sound;
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

    public final String group;
    protected Path<Vector2> path;
    protected float startTime;
    protected float originalStartTime;
    protected float pathTime;
    protected float animationTime;
    protected float totalAnimationTime;
    protected float angle;
    protected float opacity;
    public final ArrayList<Animation> animations = new ArrayList();
    protected boolean oriented;
    private float pausePath;
    protected final SceneAnimator.Getter getter;

    public Item(String group, float startTime, float angle, boolean oriented, float opacity, SceneAnimator.Getter getter) {
        this.group = group;
        this.startTime = startTime;
        originalStartTime = startTime;
        this.angle = angle;
        this.opacity = opacity;
        this.oriented = oriented;
        this.getter = getter;
    }

    public void reset(ArrayList<Animation> animations) {
        this.animations.clear();
        animations.stream().filter((anim) -> (isGroup(anim.group))).forEach((anim) -> {
            this.animations.add(anim);
        });
        startTime = originalStartTime;
        pathTime = 0;
        pausePath = 0;
        animationTime = 0;
        totalAnimationTime = 0;
        //Fixme: angle, opacity
    }
    
    public void abortPausePath() {
        pausePath = 0;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
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
        if (startTime == 0) {
            if(pausePath < 0)
                return;
            if(pausePath > 0) {
                pausePath -= deltaTime;
                if(pausePath <= 0)
                    pausePath = 0;
                else
                    return;
            }
            pathTime += deltaTime;
            if(path != null) {
                if (oriented) {
                    setAngle(path.derivativeAt(temp, pathTime).angle());
                }
                setPosition(path.valueAt(temp, pathTime));
            }

            Iterator<Animation> it = animations.iterator();
            while (it.hasNext()) {
                Animation animation = it.next();
                if (animation.time <= pathTime) {
                    startAnimation(animation);
                    it.remove();
                }
            }
        }
    }

    public abstract void render();

    public boolean isGroup(String group) {
        return group.equals("*") || this.group.equalsIgnoreCase(group);
    }

    public boolean startAnimation(Animation animation) {
        final String name = animation.animation.toLowerCase();
        if(name.equals("pause_path")) {
            pausePath = animation.animationTime;
            return true;
        } else if(name.startsWith("sound/")) {
            Sound sound = getter.getSound(animation.animation.substring(6));
            if(sound != null)
                sound.play();
            return true;
        }
        return false;
    }

    protected abstract boolean isAnimationDone();

    boolean isDone() {
        if(path == null) {
            return animations.isEmpty() && isAnimationDone();
        }
        return pathTime > path.getTotalTime();
    }
    
    boolean isStarted() {
        return startTime >= 0;
    }

    boolean shouldRender() {
        return startTime == 0 && (path == null || pathTime < path.getTotalTime());
    }
}
