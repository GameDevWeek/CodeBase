package de.hochschuletrier.gdw.commons.gdx.state.transition;

import com.badlogic.gdx.Gdx;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

/**
 * Split the screen horizontally (renders the splitting state twice)
 *
 * @author Santo Pfingsten
 */
public class SplitHorizontalTransition extends Transition {

    public SplitHorizontalTransition(int fadeTime, boolean reverse) {
        super(fadeTime, reverse);
    }

    @Override
    public void render(GameState from, GameState to) {
        int halfWidth = Gdx.graphics.getWidth() / 2;
        int fullHeight = Gdx.graphics.getHeight();
        int xOffset = Math.round(getProgress() * halfWidth);
        to.render();

        DrawUtil.setClip(0, 0, halfWidth - xOffset, fullHeight);
        DrawUtil.pushTransform();
        DrawUtil.translate(-xOffset, 0);
        from.render();
        DrawUtil.popTransform();

        DrawUtil.setClip(halfWidth + xOffset, 0, halfWidth, fullHeight);
        DrawUtil.pushTransform();
        DrawUtil.translate(xOffset, 0);
        from.render();
        DrawUtil.popTransform();
        DrawUtil.clearClip();
    }
}
