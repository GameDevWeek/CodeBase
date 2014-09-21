package de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * A camera that smoothly follows a point
 *
 * @author Santo Pfingsten
 */
public class SmoothCamera extends AbstractCamera {

    protected final Vector3 destination = new Vector3();
    protected final Vector3 moveDir = new Vector3();
    protected float factor = 10f;

    public void update(float delta) {
        moveDir.set(destination).sub(camera.position);

        float distance = moveDir.len();
        if (distance < 1) {
            camera.position.set(destination);
        } else {
            moveDir.scl(delta * factor);
            camera.position.add(moveDir);
        }
        camera.update(true);
    }

    public void updateForced() {
        setDestination(destination.x, destination.y);
        camera.position.set(destination);
        camera.update(true);
    }

    public void setDestination(float x, float y) {
        destination.x = x;
        destination.y = y;
    }

    public final void setDestination(Vector2 p) {
        setDestination(p.x, p.y);
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }
}
