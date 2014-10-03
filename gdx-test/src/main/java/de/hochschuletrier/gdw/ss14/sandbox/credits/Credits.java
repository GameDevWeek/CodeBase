package de.hochschuletrier.gdw.ss14.sandbox.credits;

import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.AnimatorData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
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
        textItem = new TextItem("Hello World, you are a LONG son of a bitch!!!", new TextStyle(assetManager.getFont("verdana", 24), Color.WHITE.cpy(), TextAlign.LEFT));
        AnimatorData.Path.Animation anim = new AnimatorData.Path.Animation();
        anim.animation = "CONSTRUCT";
        anim.animationTime = 300;
        anim.minAngle = 0;
        anim.maxAngle = 360;
        anim.minRadius = 200;
        anim.maxRadius = 300;
        anim.time = 0;
        textItem.startAnimation(anim);
        
        try {
            AnimatorData credits = JacksonReader.read("data/json/credits.json",
                    AnimatorData.class);
            System.out.println(credits.paths.size());
        } catch (Exception e) {
            e.printStackTrace();
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