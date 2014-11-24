package de.hochschuletrier.gdw.ss14.states;

import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;

public class MyBaseGameState extends BaseGameState<MyBaseGameState> {

    GameStateEnum gameStateEnum;

    public GameStateEnum getEnum() {
        return gameStateEnum;
    }
}
