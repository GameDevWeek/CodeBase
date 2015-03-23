package de.hochschuletrier.gdw.ss14.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.states.GameplayState;
import de.hochschuletrier.gdw.ss14.states.MainMenuState;

public class MenuPageRoot extends MenuPage {

    public enum Type {

        MAINMENU,
        INGAME
    }

    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");

//        addActor(new DecoImage(assetManager.getTexture("menu_bg_root_bottom")));
        int x = 100;
        int i = 0;
        int y = 370;
        int yStep = 55;
        if (type == Type.MAINMENU) {
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Spiel Starten", this::startGame);
        } else {
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Fortsetzen", () -> menuManager.popPage());
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Spiel verlassen", this::stopGame);
        }
        addPageEntry(menuManager, x, y - yStep * (i++), "Credits", new MenuPageCredits(skin, menuManager));
        addCenteredButton(menuManager.getWidth() - 80, 54, 100, 40, "Exit", () -> System.exit(-1));
    }

    private void startGame() {
        if (!main.isTransitioning()) {
            Game game = new Game();
            game.init(assetManager);
            main.changeState(new GameplayState(assetManager, game), new SplitHorizontalTransition(500), null);
        }
    }

    private void stopGame() {
        if (!main.isTransitioning()) {
            main.changeState(main.getPersistentState(MainMenuState.class));
        }
    }

    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
    }
}
