package de.hochschuletrier.gdw.ws1415.states;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.ws1415.game.Game;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState {

    private final Game game;

    public GameplayState(AssetManagerX assetManager) {
        game = new Game();
        game.init(assetManager);
    }

    @Override
    public void update(float delta) {
        game.update(delta);
    }

    @Override
    public void onEnter(BaseGameState previousState) {
    }

    @Override
    public void onLeave(BaseGameState nextState) {
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
