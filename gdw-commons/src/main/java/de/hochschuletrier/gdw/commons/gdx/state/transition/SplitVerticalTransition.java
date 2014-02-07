package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Split the screen vertically (renders the splitting state twice)
 *
 * @author Santo Pfingsten
 */
public class SplitVerticalTransition extends Transition {

    public SplitVerticalTransition(int fadeTime, boolean reverse) {
        super(fadeTime, reverse);
    }

    @Override
    public void render(GameState from, GameState to) {
        int fullWidth = Gdx.graphics.getWidth();
        int halfHeight = Gdx.graphics.getHeight() / 2;
        int yOffset = Math.round(getProgress() * 0.5f * Gdx.graphics.getHeight());

        to.render();

        DrawUtil.setClip(0, 0, fullWidth, halfHeight - yOffset);
        DrawUtil.pushTransform();
        DrawUtil.translate(0, -yOffset);
        from.render();
        DrawUtil.popTransform();

        DrawUtil.setClip(0, halfHeight + yOffset, fullWidth, halfHeight);
        DrawUtil.pushTransform();
        DrawUtil.translate(0, yOffset);
        from.render();
        DrawUtil.popTransform();
        DrawUtil.clearClip();
    }
}
