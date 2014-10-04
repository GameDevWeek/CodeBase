package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class Item {
    protected static final Vector2 temp = new Vector2();
    protected final Vector2 position = new Vector2();

    protected float startTime;
    protected float originalStartTime;
    protected float pathTime;
    protected float animationTime;
    protected float totalAnimationTime;
    protected float angle;
    protected float opacity;

    public Item(float startTime, float angle, float opacity) {
        this.startTime = startTime;
        originalStartTime = startTime;
        this.angle = angle;
        this.opacity = opacity;
    }

    public void reset() {
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

    public void update(Path<Vector2> path, float deltaTime) {
        if(startTime > 0) {
            startTime -= deltaTime;
            if(startTime < 0) {
                deltaTime = -startTime;
                startTime = 0;
            }
        }
        if(startTime == 0 && path != null) {
            pathTime += deltaTime;
            setAngle(path.derivativeAt(temp, pathTime).angle());
            setPosition(path.valueAt(temp, pathTime));
            System.out.println(temp);
        }
    }

    public abstract void render();

    public abstract void startAnimation(Animation animation);
}
