package de.hochschuletrier.gdw.ss14.sandbox.credits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.AnimatorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class Credits extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(Credits.class);
    
    private AnimatorController animatorController;
    
    public Credits() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        try {
            animatorController = new AnimatorController(assetManager, "data/json/credits.json");
        } catch (Exception ex) {
            logger.error("Error loading credits", ex);
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.DARK_GRAY);
//        textItem.render();
        if(animatorController != null)
            animatorController.render();
    }

    @Override
    public void update(float delta) {
        
//        textItem.update(null, delta);
        if(animatorController != null)
            animatorController.update(delta);
    }
}