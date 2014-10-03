package de.hochschuletrier.gdw.ss14.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.GameSettings;

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
    }
    
    private InputDevice inputDevice;
    
    public InputDevice getInputDevice() {
        return this.inputDevice;
    }
    
    public void addGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void removeGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void update() {
        inputDevice.update();
    }
    
    public static void init() {
        if (instance != null) {
            logger.info("InputManager already initialized!");
            return;
        }
        
        instance = new InputManager();
        
        switch (GameSettings.getInstance().getInputDevice()) {
            case MOUSE:
                instance.inputDevice = new InputMouse();
                break;
            case KEYBOARD:
                instance.inputDevice = new InputKeyboard();
                break;
            case GAMEPAD:
                instance.inputDevice = new InputGamePad();
                break;
        }
        Main.inputMultiplexer.addProcessor(instance.inputDevice);
    }
    
}
