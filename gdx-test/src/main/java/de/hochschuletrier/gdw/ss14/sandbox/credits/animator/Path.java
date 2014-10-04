package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Vector2;

public interface Path<T> {

    Vector2 derivativeAt(Vector2 out, float t);

    Vector2 valueAt(Vector2 out, float t);

    float getTotalTime();
}
