package de.hochschuletrier.gdw.ss14.sandbox.credits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class Credits extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(Credits.class);

    private Vector2 start;
    private Vector2 control;
    private Vector2 end;
    private Vector2 current = new Vector2();
    private Vector2 tmp = new Vector2();
    private float stateTime = 0;
    
    public Credits() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        start = new Vector2(0, 0);
        control = new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        end = new Vector2(Gdx.graphics.getWidth(), 0);
        
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
        float curveTime = 1.0f;
        Bezier.quadratic(current, (stateTime%curveTime)/curveTime, start, control, end, tmp);
        DrawUtil.fillRect(current.x-5, current.y-5, 10, 10, Color.RED);
        System.out.println(current.x + "/" + current.y);
    }

    @Override
    public void update(float delta) {
        stateTime += delta;
    }
}
