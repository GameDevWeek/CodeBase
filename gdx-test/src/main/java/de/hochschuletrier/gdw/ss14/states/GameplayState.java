package de.hochschuletrier.gdw.ss14.states;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss14.game.Game;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends MyBaseGameState {

    private Game game;
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        game = new Game();
        game.init(assetManager);
    }

    @Override
    public void update(float delta) {
        game.update(delta);
        fpsCalc.addFrame();
    }

    @Override
    public void onEnter(MyBaseGameState previousState) {
    }

    @Override
    public void onLeave(MyBaseGameState nextState) {
    }

    @Override
    public void dispose() {
    }
}
