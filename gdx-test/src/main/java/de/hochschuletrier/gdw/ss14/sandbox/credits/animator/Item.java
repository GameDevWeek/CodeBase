package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Santo Pfingsten
 */
public abstract class Item {
    public final Vector2 position = new Vector2(200, 400);

    public float startTime;
    public float animationTime;
    public float totalAnimationTime;
    public float angle;
    public float opacity;

    public abstract void update(float deltaTime);

    public abstract void render();

    public abstract void startAnimation(AnimatorData.Path.Animation animation);
}
