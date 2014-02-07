package de.hochschuletrier.gdw.commons.gdx.state;

import com.badlogic.gdx.assets.AssetManager;

/**
 * The most basic game state
 *
 * @author Santo Pfingsten
 */
public class GameState {

    protected AssetManager assetManager;

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void render() {
    }

    public void update(int delta) {
    }

    public void enter() {
    }

    public void leave() {
    }

    public void showMenu() {
    }

    public void hideMenu() {
    }

    public void dispose() {
    }
}
