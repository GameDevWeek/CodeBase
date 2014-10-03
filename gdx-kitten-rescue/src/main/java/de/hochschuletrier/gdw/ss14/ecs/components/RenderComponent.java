package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author David Neubauer
 */
public class RenderComponent implements Component {
    
    /**
     * A texture that will be rendered.
     */
    public TextureRegion texture;
    
    /**
     * Change this attribute to push something to the background / bring something to the foreground.
     * GREATER numbers move the object more to the FOREGROUND.
     * Negative values are possible.
     */
    public int zIndex = 0;
    
    // This color will be added to the whole texture
    // null means: draw the texture normally
    public Color tintColor = null;
}
