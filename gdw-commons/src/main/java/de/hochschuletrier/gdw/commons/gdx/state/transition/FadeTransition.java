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
public class FadeTransition extends Transition<FadeTransition> {

    private Color color;

    public FadeTransition() {
        this(Color.BLACK, 500);
    }

    public FadeTransition(Color color) {
        this(color, 500);
    }

    public FadeTransition(Color color, int fadeTime) {
        super(fadeTime);

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
