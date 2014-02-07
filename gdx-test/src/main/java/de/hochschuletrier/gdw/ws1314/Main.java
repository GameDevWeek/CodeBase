package de.hochschuletrier.gdw.ws1314;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.ImageXLoader;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.SleepDummyLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 *
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {

    private final AssetManager assetManager = new AssetManager();
    private OrthographicCamera camera;
    private static Main instance;

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }
    public static AssetManager getManager() {
        return getInstance().assetManager;
    }

    private void setupManager() {
        InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager.setLoader(ImageX.class, new ImageXLoader(fileHandleResolver));
        
        ImageXLoader.ImageXParameter param = new ImageXLoader.ImageXParameter();
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;
        assetManager.load("data/libgdx.png", ImageX.class, param);
        
        // Just adding some sleep dummies for a progress bar test
        assetManager.setLoader(SleepDummyLoader.SleepDummy.class, new SleepDummyLoader(fileHandleResolver));
        SleepDummyLoader.SleepDummyParameter dummyParam = new SleepDummyLoader.SleepDummyParameter(200);
        for(int i=0; i<100; i++)
            assetManager.load("dummy" + i, SleepDummyLoader.SleepDummy.class, dummyParam);
    }
    
    private void setupGdx() {
        KeyUtil.init();
        Texture.setEnforcePotImages(false);
//        Gdx.graphics.setTitle("LibGDX Test");
//        Gdx.graphics.setDisplayMode(512, 256, false);
        Gdx.graphics.setContinuousRendering(true);
        // Disable VSync for the loading state, to speed things up
        // This will be enabled when loading is done
        Gdx.graphics.setVSync(false);

        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setCatchBackKey(true);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        GameStates.LOADING.init(assetManager);
        GameStates.LOADING.activate();
    }
    
    @Override
    public void create() {
        setupManager();
        setupGdx();
    }

    @Override
    public void dispose() {
        DrawUtil.batch.dispose();
        GameStates.dispose();
    }


    @Override
    protected void preRender() {
        DrawUtil.clearColor(Color.BLACK);
        DrawUtil.clear();

        DrawUtil.updateCamera(camera);
        DrawUtil.batch.begin();
    }

    @Override
    protected void postRender() {
        DrawUtil.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        DrawUtil.init(width, height);

        camera = new OrthographicCamera(width, height);
        camera.setToOrtho(true, width, height);
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        DrawUtil.updateCamera(camera);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "LibGDX Test";
        cfg.useGL20 = false;
        cfg.width = 512;
        cfg.height = 256;

        new LwjglApplication(getInstance(), cfg);
    }
}
