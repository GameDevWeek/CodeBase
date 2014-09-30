package de.hochschuletrier.gdw.ss14.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ss14.game.GameSettings;
import de.hochschuletrier.gdw.ss14.sandbox.inputTest.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.sandbox.inputTest.GeneralInputAdapter;

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
        switch (GameSettings.getInstance().getInputDevice()) {
        case MOUSE:
            break;
        case KEYBOARD:
            break;
        case GAMEPAD:
            break;
        }
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
    
    public static void init() {
        if (instance != null) {
            logger.info("InputManager already initialized!");
            return;
        }
        instance = new InputManager();
    }
    
}
