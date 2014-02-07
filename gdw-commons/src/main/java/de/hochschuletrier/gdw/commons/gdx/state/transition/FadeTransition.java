package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Fade to/from a color (default black)
 *
 * @author Santo Pfingsten
 */
public class FadeTransition extends Transition {

    private Color color;

    public FadeTransition(boolean fadeIn) {
        this(Color.BLACK, 500, fadeIn);
    }

    public FadeTransition(Color color, boolean fadeIn) {
        this(color, 500, fadeIn);
    }

    public FadeTransition(Color color, int fadeTime, boolean fadeIn) {
        super(fadeTime, fadeIn);

        this.color = new Color(color);
        this.color.a = getProgress();
    }

    @Override
    public void render(GameState from, GameState to) {
        from.render();

        color.a = getProgress();
        Color old = DrawUtil.getColor();
        DrawUtil.setColor(color);
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        DrawUtil.setColor(old);
    }
}
