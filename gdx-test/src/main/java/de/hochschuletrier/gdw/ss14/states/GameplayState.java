package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.game.GameConstants;
import de.hochschuletrier.gdw.ss14.menu.MenuPageRoot;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState {

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);

    private final Game game;
    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, this::onMenuEmptyPop);
    private final InputForwarder inputForwarder;
    private final InputProcessor menuInputProcessor;
    private final InputProcessor gameInputProcessor;

    public GameplayState(AssetManagerX assetManager, Game game) {
        this.game = game;

        music = assetManager.getMusic("gameplay");

        Skin skin = ((MainMenuState)Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        final MenuPageRoot menuPageRoot = new MenuPageRoot(skin, menuManager, MenuPageRoot.Type.INGAME);
        menuManager.addLayer(menuPageRoot);
        menuInputProcessor = menuManager.getInputProcessor();
        gameInputProcessor = game.getInputProcessor();

        menuManager.addLayer(new DecoImage(assetManager.getTexture("menu_fg")));
        menuManager.pushPage(menuPageRoot);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (mainProcessor == gameInputProcessor) {
                        mainProcessor = menuInputProcessor;
                    } else {
                        menuManager.popPage();
                    }
                    return true;
                }
                return super.keyUp(keycode);
            }
        };
    }

    private void onMenuEmptyPop() {
        inputForwarder.set(gameInputProcessor);
    }

    @Override
    public void update(float delta) {
        game.update(delta);
        if (inputForwarder.get() == menuInputProcessor) {
            menuManager.update(delta);
            Main.getInstance().screenCamera.bind();
            DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), OVERLAY_COLOR);
            menuManager.render();
        }
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
    }

    @Override
    public void onEnterComplete() {
        Main.inputMultiplexer.addProcessor(inputForwarder);
        inputForwarder.set(gameInputProcessor);
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        Main.inputMultiplexer.removeProcessor(inputForwarder);
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
