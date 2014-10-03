package de.hochschuletrier.gdw.ss14.states;

import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;

public class KittenGameState extends BaseGameState<KittenGameState> {
    GameStateEnum gameStateEnum;
    
    public GameStateEnum getEnum() {
        return gameStateEnum;
    }
}
