package de.hochschuletrier.gdw.commons.gdx.state;

import de.hochschuletrier.gdw.commons.gdx.assetloaders.AssetManagerX;

/**
 * The most basic game state
 *
 * @author Santo Pfingsten
 */
public class GameState {

    protected AssetManagerX assetManager;

    public void init(AssetManagerX assetManager) {
        this.assetManager = assetManager;
    }

    public void render() {
    }

    public void update(float delta) {
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
