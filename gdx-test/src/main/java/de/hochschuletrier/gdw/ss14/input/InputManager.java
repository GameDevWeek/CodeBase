package de.hochschuletrier.gdw.ss14.input;

import de.hochschuletrier.gdw.ss14.sandbox.inputTest.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.sandbox.inputTest.GeneralInputAdapter;

public class InputManager {
    private static InputManager instance;
    
    public static InputManager getInstance () {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }
    
    private InputManager(){
    }
    
    private GeneralInputAdapter inputDevice = new GeneralInputAdapter();
    
    public void addGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void removeGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void update() {
        
    }
    
    
}
