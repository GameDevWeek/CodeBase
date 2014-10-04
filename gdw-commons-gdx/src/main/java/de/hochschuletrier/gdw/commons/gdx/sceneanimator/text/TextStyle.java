package de.hochschuletrier.gdw.commons.gdx.sceneanimator.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 * @author Santo Pfingsten
 */
public class TextStyle {

    public final BitmapFont font;
    public final Color color;
    public final TextAlign align;

    public TextStyle(BitmapFont font, Color color, TextAlign align) {
        this.font = font;
        this.color = color;
        this.align = align;
    }
}
