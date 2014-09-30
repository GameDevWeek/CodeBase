package de.hochschuletrier.gdw.ss14.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputManager {
    private static final Logger logger = LoggerFactory.getLogger(InputManager.class);
    private static InputManager instance;
    
    public static InputManager getInstance () {
        if (instance == null) {
            logger.error("InputManager not initialized!");
        }
        return instance;
    }
    
    private InputManager(){
        
        /*
        switch (GameSettings.getInstance().getInputDevice()) {
        case MOUSE:
            break;
        case KEYBOARD:
            break;
        case GAMEPAD:
            break;
        }
        */
    }
    
    private InputDevice inputDevice = new InputMouse();
    
    public void addGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void removeGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void update() {
        
    }
    
    public static void init() {
        if (instance != null) {
            logger.info("InputManager already initialized!");
            return;
        }
        instance = new InputManager();
    }
    
}
