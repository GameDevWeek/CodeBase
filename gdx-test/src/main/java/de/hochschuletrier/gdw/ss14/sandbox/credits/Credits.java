package de.hochschuletrier.gdw.ss14.sandbox.credits;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
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

    public Credits() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        
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
    }

    @Override
    public void update(float delta) {
    }
}
