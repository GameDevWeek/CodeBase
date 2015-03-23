package de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.commons.gdx.state.ScreenListener;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Base class for orthographic camera controllers.
 *
 * @author Santo Pfingsten
 */
public abstract class AbstractCamera implements ScreenListener {

    protected final OrthographicCamera camera = new OrthographicCamera() {
        public void setToOrtho(boolean yDown, float viewportWidth, float viewportHeight) {
            if (yDown) {
                up.set(0, -1, 0);
                direction.set(0, 0, 1);
            } else {
                up.set(0, 1, 0);
                direction.set(0, 0, -1);
            }
            this.viewportWidth = viewportWidth;
            this.viewportHeight = viewportHeight;
            onViewportChanged(viewportWidth, viewportHeight);
            update(true);
        }
    };

    @Override
    public final void resize(int width, int height) {
        camera.setToOrtho(true, width, height);
    }

    protected void onViewportChanged(float width, float height) {

    }

    public void update(float delta) {
    }

    public void setCameraPosition(float x, float y) {
        camera.position.x = Math.round(x);
        camera.position.y = Math.round(y);
    }

    public void setCameraPosition(Vector3 pos) {
        camera.position.x = Math.round(pos.x);
        camera.position.y = Math.round(pos.y);
        camera.position.z = Math.round(pos.z);
    }

    public void setZoom(float newZoom) {
        camera.zoom = newZoom;
    }

    public float getZoom() {
        return camera.zoom;
    }

    public float getLeftOffset() {
        return camera.position.x - camera.viewportWidth / 2;
    }

    public float getTopOffset() {
        return camera.position.y - camera.viewportHeight / 2;
    }

    public final void bind() {
        DrawUtil.batch.setProjectionMatrix(camera.combined);
    }
}
