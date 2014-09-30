package de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal;

/**
 * A smooth camera controller that stays within the set bounds.
 * If the camera width or height exceeds the bounds, it will be centered on the bounds.
 *
 * @author Santo Pfingsten
 */
public class LimitedSmoothCamera extends SmoothCamera {

    float xMin, yMin, xMax, yMax;
    
    @Override
    protected void onViewportChanged(float width, float height) {
        updateForced();
    }

    @Override
    public void setDestination(float x, float y) {
        destination.x = clamp(x, xMin, xMax, camera.viewportWidth);
        destination.y = clamp(y, yMin, yMax, camera.viewportHeight);
    }

    private float clamp(float in, float min, float max, float viewportSize) {
        if ((max - min) <= viewportSize) {
            return min + (max - min) / 2;
        }
        min += viewportSize / 2;
        if (in < min) {
            return min;
        }
        max -= viewportSize / 2;
        if (in > max) {
            return max;
        }
        return in;
    }

    public void setBounds(float xMin, float yMin, float xMax, float yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }
}
