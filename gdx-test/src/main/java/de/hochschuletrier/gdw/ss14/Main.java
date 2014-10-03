package de.hochschuletrier.gdw.ss14;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.TrueTypeFont;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.ss14.sandbox.credits.animator.TextAnimation;
import de.hochschuletrier.gdw.ss14.states.GameStates;

/**
 * 
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1024;
    public static final CVarEnum<TextAnimation> textAnimation = new CVarEnum("text_animation", TextAnimation.CONSTRUCT_TYPE, TextAnimation.class, 0, "text animation type");
    public static final CVarInt animationTime = new CVarInt("animation_time", 200, 0, 100000, 0, "text animation type");

    private final AssetManagerX assetManager = new AssetManagerX();
    private static Main instance;

    public final DevConsole console = new DevConsole(16);
    private final DevConsoleView consoleView = new DevConsoleView(console);
    private Skin skin;
    public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final CVarEnum<SoundDistanceModel> distanceModel = new CVarEnum("snd_distanceModel", SoundDistanceModel.INVERSE, SoundDistanceModel.class, 0, "sound distance model");
    private final CVarEnum<SoundEmitter.Mode> emitterMode = new CVarEnum("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    private void setupDummyLoader() {
    }

    private void loadAssetLists() {
        TextureParameter param = new TextureParameter();
        param.minFilter = param.magFilter = Texture.TextureFilter.Linear;

        assetManager.loadAssetList("data/json/images.json", Texture.class, param);
        assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
        assetManager.loadAssetList("data/json/music.json", Music.class, null);
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
                AnimationExtendedLoader.AnimationExtendedParameter.class);
        BitmapFontParameter fontParam = new BitmapFontParameter();
        fontParam.flip = true;
        assetManager.loadAssetList("data/json/fonts_bitmap.json", BitmapFont.class,
                fontParam);
        assetManager.loadAssetList("data/json/fonts_truetype.json", TrueTypeFont.class,
                null);
    }

    private void setupGdx() {
        KeyUtil.init();
        Gdx.graphics.setContinuousRendering(true);

        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void create() {
        CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Local));
        DrawUtil.init();
        setupDummyLoader();
        loadAssetLists();
        setupGdx();
        skin = new Skin(Gdx.files.internal("data/skins/basic.json"));
        consoleView.init(assetManager, skin);
        addScreenListener(consoleView);
        inputMultiplexer.addProcessor(consoleView.getInputProcessor());

        GameStates.LOADING.init(assetManager);
        GameStates.LOADING.activate();

        this.console.register(distanceModel);
        distanceModel.addListener((CVar)->distanceModel.get().activate());

        this.console.register(emitterMode);
        emitterMode.addListener(this::onEmitterModeChanged);
        
        this.console.register(textAnimation);
        this.console.register(animationTime);
    }

    public void onLoadComplete() {
        for(GameStates state: GameStates.values()) {
            if(state != GameStates.LOADING) {
                state.init(assetManager);
            }
        }
        GameStates.MAINMENU.activate();
//		GameStates.GAMEPLAY.activate(new SplitVerticalTransition(500), null);
    }

    @Override
    public void dispose() {
        DrawUtil.batch.dispose();
        GameStates.dispose();
        consoleView.dispose();
        skin.dispose();
        SoundEmitter.disposeGlobal();
    }

    @Override
    protected void preRender() {
        DrawUtil.clearColor(Color.BLACK);
        DrawUtil.clear();
        DrawUtil.resetColor();

        DrawUtil.batch.begin();
    }

    @Override
    protected void postRender() {
        DrawUtil.batch.end();
        if (consoleView.isVisible()) {
            consoleView.render();
        }
    }

    @Override
    protected void preUpdate(float delta) {
        if (consoleView.isVisible()) {
            consoleView.update(delta);
        }
        console.executeCmdQueue();
        SoundEmitter.updateGlobal();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        SoundEmitter.setListenerPosition(width / 2, height / 2, 10, emitterMode.get());
    }

    public void onEmitterModeChanged(CVar cvar) {
        int x = Gdx.graphics.getWidth() / 2;
        int y = Gdx.graphics.getHeight() / 2;
        SoundEmitter.setListenerPosition(x, y, 10, emitterMode.get());
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
        cfg.width = WINDOW_WIDTH;
        cfg.height = WINDOW_HEIGHT;
        cfg.useGL30 = false;
        cfg.vSyncEnabled = false;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;

        new LwjglApplication(getInstance(), cfg);
    }
}
