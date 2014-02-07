package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitVerticalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;

public class LoadGameState extends GameState {

    private boolean isDone;

    @Override
    public void init(AssetManager assetManager) {
        super.init(assetManager);
    }

    @Override
    public void render() {
        DrawUtil.setColor(Color.BLACK);
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        DrawUtil.setColor(Color.GREEN);
        float drawWidth = Gdx.graphics.getWidth() - 100.0f;
        DrawUtil.fillRect(50, Gdx.graphics.getHeight() / 2 - 25, (int) (drawWidth * assetManager.getProgress()), 50);
        DrawUtil.drawRect(50, Gdx.graphics.getHeight() / 2 - 25, drawWidth, 50);
        DrawUtil.resetColor();
    }

    @Override
    public void update(int delta) {
        if (isDone) {
            return;
        }

        assetManager.update();

        if (assetManager.getProgress() == 1) {
            // VSync was only disabled to speed up loading
            Gdx.graphics.setVSync(true);

            GameStates.MAINMENU.init(assetManager);
            GameStates.MAINMENU.activate(new SplitVerticalTransition(500).reverse(), null);
            isDone = true;
        }
    }
    
    @Override
    public void dispose() {
    }
}
