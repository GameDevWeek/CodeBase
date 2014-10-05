package de.hochschuletrier.gdw.commons.gdx.sceneanimator;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Santo Pfingsten
 */
public interface Path<T> {

    Vector2 derivativeAt(Vector2 out, float t);

    Vector2 valueAt(Vector2 out, float t);

    float getTotalTime();
}
