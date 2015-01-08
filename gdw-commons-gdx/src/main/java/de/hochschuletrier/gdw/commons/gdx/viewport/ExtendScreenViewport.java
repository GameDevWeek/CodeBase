/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.hochschuletrier.gdw.commons.gdx.viewport;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

/** A viewport that behaves like ExtendViewport if below the ideal size and like ScreenViewport otherwise.
 * This was made to allow a menu to be centered on the screen, but scaled down if the screen is smaller than the menu was made for.
 * @author Santo Pfingsten (ExtendScreenViewport)
 * @author Daniel Holderbaum (ScreenViewport)
 * @author Nathan Sweet (ExtendViewport, ScreenViewport) */
public class ExtendScreenViewport extends Viewport {

    private float idealWidth, idealHeight;

    /** Creates a new viewport using a new {@link OrthographicCamera}. */
    public ExtendScreenViewport(float idealWidth, float idealHeight) {
        this(idealWidth, idealHeight, new OrthographicCamera());
    }

    /** Creates a new viewport */
    public ExtendScreenViewport(float idealWidth, float idealHeight, Camera camera) {
        this.idealWidth = idealWidth;
        this.idealHeight = idealHeight;
        setCamera(camera);
    }

    @Override
    public void update(int screenWidth, int screenHeight, boolean centerCamera) {
        if (screenWidth >= idealWidth && screenHeight >= idealHeight) {
            setScreenBounds(0, 0, screenWidth, screenHeight);
            setWorldSize(screenWidth, screenHeight);
        } else {
            // Fit min size to the screen.
            float worldWidth = idealWidth;
            float worldHeight = idealHeight;
            Vector2 scaled = Scaling.fit.apply(worldWidth, worldHeight, screenWidth, screenHeight);

            // Extend in the short direction.
            int viewportWidth = Math.round(scaled.x);
            int viewportHeight = Math.round(scaled.y);
            if (viewportWidth < screenWidth) {
                float toViewportSpace = viewportHeight / worldHeight;
                float toWorldSpace = worldHeight / viewportHeight;
                float lengthen = (screenWidth - viewportWidth) * toWorldSpace;
                worldWidth += lengthen;
                viewportWidth += Math.round(lengthen * toViewportSpace);
            } else if (viewportHeight < screenHeight) {
                float toViewportSpace = viewportWidth / worldWidth;
                float toWorldSpace = worldWidth / viewportWidth;
                float lengthen = (screenHeight - viewportHeight) * toWorldSpace;
                worldHeight += lengthen;
                viewportHeight += Math.round(lengthen * toViewportSpace);
            }

            setWorldSize(worldWidth, worldHeight);

            // Center.
            setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
        }
        apply(centerCamera);
    }
}
