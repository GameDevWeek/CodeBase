package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.GameConstants;
import de.hochschuletrier.gdw.ss14.menu.MenuPageRoot;

/**
 * Menu state
 *
 * @author Santo Pfingsten
 */
public class MainMenuState extends BaseGameState {

    private final Skin skin = new Skin(Gdx.files.internal("data/ui/menu/skins/menu.json"));
    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, null);
    private final InputForwarder inputForwarder;

    public MainMenuState(AssetManagerX assetManager) {
        music = assetManager.getMusic("menu");

        final MenuPageRoot menuPageRoot = new MenuPageRoot(skin, menuManager, MenuPageRoot.Type.MAINMENU);
        menuManager.addLayer(menuPageRoot);

        menuManager.addLayer(new DecoImage(assetManager.getTexture("menu_fg")));
        menuManager.pushPage(menuPageRoot);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {
            @Override
            public boolean keyUp(int keycode) {
                if (mainProcessor != null && keycode == Input.Keys.ESCAPE) {
                    menuManager.popPage();
                    return true;
                }
                return super.keyUp(keycode);
            }
        };

        Main.inputMultiplexer.addProcessor(inputForwarder);
    }

    public void render() {
        Main.getInstance().screenCamera.bind();
        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);
        menuManager.render();
    }

    @Override
    public void update(float delta) {
        menuManager.update(delta);
        render();
    }

    @Override
    public void onEnterComplete() {
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
        inputForwarder.set(menuManager.getInputProcessor());
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        inputForwarder.set(null);
    }

    @Override
    public void dispose() {
        menuManager.dispose();
    }

    /**
     * @return the skin
     */
    public Skin getSkin() {
        return skin;
    }
}
