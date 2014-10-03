package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class Item {
    protected final Vector2 position = new Vector2();

    protected float startTime;
    protected float animationTime;
    protected float totalAnimationTime;
    protected float angle;
    protected float opacity;

    public Item(float startTime, float angle, float opacity) {
        this.startTime = startTime;
        this.angle = angle;
        this.opacity = opacity;
    }
    
    public void setPosition(Vector2 pos) {
        position.set(pos);
    }

    public abstract void update(float deltaTime);

    public abstract void render();

    public abstract void startAnimation(Animation animation);
}
