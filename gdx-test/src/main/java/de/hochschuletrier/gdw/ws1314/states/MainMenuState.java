package de.hochschuletrier.gdw.ws1314.states;

import com.badlogic.gdx.assets.AssetManager;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.ws1314.Main;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends GameState {
    private ImageX image;

    public MainMenuState() {
    }

    @Override
    public void init(AssetManager assetManager) {
        super.init(assetManager);
        
        image = Main.getManager().get("data/libgdx.png", ImageX.class);
    }

    @Override
    public void render() {
        image.draw(0, 0, 512, 256, 0, 0);
    }

    @Override
    public void update(int delta) {
    }

    @Override
    public void enter() {
    }

    @Override
    public void leave() {
    }
    
    @Override
    public void dispose() {
        image.dispose();
    }
}