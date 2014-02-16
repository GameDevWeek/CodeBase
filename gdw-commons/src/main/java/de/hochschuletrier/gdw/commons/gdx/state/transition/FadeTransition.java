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
    private float threshold;

    public FadeTransition() {
        this(Color.BLACK, 500, 0.5f);
    }

    public FadeTransition(Color color) {
        this(color, 500, 0.5f);
    }

    public FadeTransition(Color color, int fadeTime) {
        this(color, fadeTime, 0.5f);
        
    }

    public FadeTransition(Color color, int fadeTime, float threshold) {
        super(fadeTime);

        this.color = new Color(color);
        this.threshold = threshold;
    }

    @Override
    public void render(GameState from, GameState to) {
        float progress = getProgress();
        if(progress < threshold) {
            color.a = progress / threshold;
            from.render();
        } else {
            color.a = (1.0f - progress) / (1.0f-threshold);
            to.render();
        }

        DrawUtil.setColor(color);
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
