package de.hochschuletrier.gdw.ss14.sandbox.credits;

import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.AnimatorData;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
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
    
    private TextItem textItem;
    
    public Credits() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        textItem = new TextItem("Laser Chaser: Zombie Racer", new TextStyle(assetManager.getFont("verdana", 24), Color.WHITE.cpy(), TextAlign.LEFT));
        textItem.setPosition(new Vector2(200, 400));
        AnimatorData.Path.Animation anim = new AnimatorData.Path.Animation();
        anim.animation = Main.textAnimation.get().name();
        anim.animationTime = Main.animationTime.get();
        anim.minAngle = 0;
        anim.maxAngle = 360;
        anim.minRadius = 200;
        anim.maxRadius = 300;
        anim.time = 0;
        textItem.startAnimation(anim);
        
        try {
            AnimatorController ctrl = new AnimatorController(assetManager, "data/json/credits.json");
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
        textItem.render();
    }

    @Override
    public void update(float delta) {
        
        textItem.update(delta);
    }
}