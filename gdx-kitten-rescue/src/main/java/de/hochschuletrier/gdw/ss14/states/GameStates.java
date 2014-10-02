package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.state.transition.FadeTransition;
import de.hochschuletrier.gdw.commons.gdx.state.transition.Transition;
import de.hochschuletrier.gdw.ss14.Main;

public enum GameStates {

    LOADING(new LoadGameState()),
    MAINMENU(new MainMenuState()),
    GAMEPLAY(new GameplayState()),
    LEVELMENU(new GameplayState()),
    SANDBOX(new SandboxState()),
    PAUSEGAME(new PauseGameState()),
    OPTIONSMENU(new OptionsMenuState());
    private final GameState state;

    GameStates(GameState state) {
        this.state = state;
    }

    public void activate() {
        Main.getInstance().changeState(state, null, null);
    }

    public void activate(int fadeTime) {
        FadeTransition out = new FadeTransition(Color.BLACK, fadeTime);
        Main.getInstance().changeState(state, out, null);
    }

    public void activate(Transition out, Transition in) {
        Main.getInstance().changeState(state, out, in);
    }

    public GameState get() {
        return state;
    }

    public void init(AssetManagerX assetManager) {
        state.init(assetManager);
    }

    public static void dispose() {
        for (GameStates entry : GameStates.values()) {
            entry.state.dispose();
        }
    }

    public boolean isActive() {
        return Main.getInstance().getCurrentState() == state;
    }
}
