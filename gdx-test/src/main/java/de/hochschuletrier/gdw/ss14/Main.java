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
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxCommand;
import de.hochschuletrier.gdw.ss14.states.LoadGameState;
import de.hochschuletrier.gdw.ss14.states.MainMenuState;

/**
 * 
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame {

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1024;

    private final AssetManagerX assetManager = new AssetManagerX();
    private static Main instance;

    public final DevConsole console = new DevConsole(16);
    private final DevConsoleView consoleView = new DevConsoleView(console);
    private Skin skin;
    public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final CVarEnum<SoundDistanceModel> distanceModel = new CVarEnum("snd_distanceModel", SoundDistanceModel.INVERSE, SoundDistanceModel.class, 0, "sound distance model");
    private final CVarEnum<SoundEmitter.Mode> emitterMode = new CVarEnum("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");

    public Main() {
        super(new BaseGameState());
    }

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
        assetManager.loadAssetList("data/json/fonts.json", BitmapFont.class, fontParam);
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
        consoleView.init(skin);
        addScreenListener(consoleView);
        inputMultiplexer.addProcessor(consoleView.getInputProcessor());

        changeState(new LoadGameState(assetManager, this::onLoadComplete), null, null);

        this.console.register(distanceModel);
        distanceModel.addListener((CVar) -> distanceModel.get().activate());

        this.console.register(emitterMode);
        emitterMode.addListener(this::onEmitterModeChanged);
    }

    private void onLoadComplete() {
        final MainMenuState mainMenuState = new MainMenuState(assetManager);
        addPersistentState(mainMenuState);
        changeState(mainMenuState, null, null);
        SandboxCommand.init(assetManager);
    }

    @Override
    public void dispose() {
        super.dispose();
        DrawUtil.batch.dispose();
        consoleView.dispose();
        skin.dispose();
        SoundEmitter.disposeGlobal();
    }

    protected void preRender() {
        DrawUtil.clearColor(Color.BLACK);
        DrawUtil.clear();
        DrawUtil.resetColor();

        DrawUtil.batch.begin();
    }

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

        preRender();
    }

    @Override
    protected void postUpdate(float delta) {
        postRender();
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
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;

        new LwjglApplication(getInstance(), cfg);
    }
}
