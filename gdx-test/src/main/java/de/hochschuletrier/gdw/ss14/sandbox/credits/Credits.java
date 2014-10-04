package de.hochschuletrier.gdw.ss14.sandbox.credits;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.Animation;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.AnimatorController;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.TextAlign;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.TextItem;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.TextStyle;
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