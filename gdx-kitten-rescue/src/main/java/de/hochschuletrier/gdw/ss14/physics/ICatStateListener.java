package de.hochschuletrier.gdw.ss14.physics;

import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

public interface ICatStateListener {
    
    public void stateChanged(CatStateEnum oldstate, CatStateEnum newstate);

}
