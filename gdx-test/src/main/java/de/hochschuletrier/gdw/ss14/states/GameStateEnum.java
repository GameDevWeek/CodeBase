package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.state.transition.FadeTransition;
import de.hochschuletrier.gdw.commons.gdx.state.transition.Transition;
import de.hochschuletrier.gdw.ss14.Main;

public enum GameStateEnum {

    LOADING(new LoadGameState()),
    MAINMENU(new MainMenuState()),
    GAMEPLAY(new GameplayState()),
    SANDBOX(new SandboxState());
    private final MyBaseGameState state;

    GameStateEnum(MyBaseGameState state) {
        this.state = state;
        state.gameStateEnum = this;
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

    public MyBaseGameState get() {
        return state;
    }

    public void init(AssetManagerX assetManager) {
        state.init(assetManager);
    }

    public static void dispose() {
        for (GameStateEnum entry : GameStateEnum.values()) {
            entry.state.dispose();
        }
    }

    boolean isActive() {
        return Main.getInstance().getCurrentState() == state;
    }
}
