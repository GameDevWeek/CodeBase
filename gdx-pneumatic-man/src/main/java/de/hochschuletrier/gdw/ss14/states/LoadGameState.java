package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;

public class LoadGameState extends BaseGameState {

    private boolean isDone;
    private final AssetManagerX assetManager;
    private final Runnable completeFunc;

    public LoadGameState(AssetManagerX assetManager, Runnable completeFunc) {
        this.assetManager = assetManager;
        this.completeFunc = completeFunc;
    }

    public void render() {
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);

        float drawWidth = Gdx.graphics.getWidth() - 100.0f;
        DrawUtil.fillRect(50, Gdx.graphics.getHeight() / 2 - 25, (int) (drawWidth * assetManager.getProgress()), 50, Color.GREEN);
        DrawUtil.drawRect(50, Gdx.graphics.getHeight() / 2 - 25, drawWidth, 50, Color.GREEN);
    }

    @Override
    public void update(float delta) {
        if (!isDone) {
            assetManager.update();

            if (assetManager.getProgress() == 1) {
                completeFunc.run();
                isDone = true;
            }
        }

        render();
    }

    @Override
    public void dispose() {
    }
}
