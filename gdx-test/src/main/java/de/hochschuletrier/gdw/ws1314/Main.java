package de.hochschuletrier.gdw.ws1314;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.AnimationXLoader;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.FontXLoader;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.ImageXLoader;
import de.hochschuletrier.gdw.commons.gdx.assetloaders.SleepDummyLoader;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationX;
import de.hochschuletrier.gdw.commons.gdx.assets.FontX;
import de.hochschuletrier.gdw.commons.gdx.assets.ImageX;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 *
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 512;

    private final AssetManagerX assetManager = new AssetManagerX();
    private OrthographicCamera camera;
    private static Main instance;

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    private void setupDummyLoader() {
        // Just adding some sleep dummies for a progress bar test
        InternalFileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
        assetManager.setLoader(SleepDummyLoader.SleepDummy.class, new SleepDummyLoader(fileHandleResolver));
        SleepDummyLoader.SleepDummyParameter dummyParam = new SleepDummyLoader.SleepDummyParameter(100);
        for(int i=0; i<100; i++)
            assetManager.load("dummy" + i, SleepDummyLoader.SleepDummy.class, dummyParam);
    }
    
    private void loadAssetLists() {
        ImageXLoader.ImageXParameter imageParam = new ImageXLoader.ImageXParameter();
        imageParam.minFilter = Texture.TextureFilter.Linear;
        imageParam.magFilter = Texture.TextureFilter.Linear;
        assetManager.loadAssetList("data/json/images.json", ImageX.class, imageParam);
        
        assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
        assetManager.loadAssetList("data/json/music.json", Music.class, null);
        
        FontXLoader.FontXParameter fontParam = new FontXLoader.FontXParameter();
        fontParam.minFilter = Texture.TextureFilter.Linear;
        fontParam.magFilter = Texture.TextureFilter.Linear;
        fontParam.flip = true;
        assetManager.loadAssetList("data/json/fonts.json", FontX.class, fontParam);
        
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationX.class, AnimationXLoader.AnimationXParameter.class);
    }
    
    private void setupGdx() {
        KeyUtil.init();
        Texture.setEnforcePotImages(false);
//        Gdx.graphics.setTitle("LibGDX Test");
//        Gdx.graphics.setDisplayMode(WINDOW_WIDTH, WINDOW_HEIGHT, false);
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
        CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Internal));
        setupDummyLoader();
        loadAssetLists();
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
        cfg.width = WINDOW_WIDTH;
        cfg.height = WINDOW_HEIGHT;

        new LwjglApplication(getInstance(), cfg);
    }
}
