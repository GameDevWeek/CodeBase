package de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal;

/**
 * A fixed camera that is centered on the screen.
 * This can be used to draw UI and HUD for example.
 *
 * @author Santo Pfingsten
 */
public class ScreenCamera extends AbstractCamera {

    public void onViewportChanged(float width, float height) {
        camera.position.set(width / 2, height / 2, 0);
    }
}
