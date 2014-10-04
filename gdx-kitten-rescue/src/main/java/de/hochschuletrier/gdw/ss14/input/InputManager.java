package de.hochschuletrier.gdw.ss14.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.GameSettings;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;

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
        
        instance.changeInputDevice(GameSettings.getInstance().getInputDeviceType());
    }
    
    public void registerProcessor() {
        inputDevice.registerProcessor();
    }
    
    public void unregisterProcessor() {
        inputDevice.unregisterProcessor();
    }
    
    public void changeInputDevice(DeviceType deviceType) {
        if (inputDevice != null) {
            unregisterProcessor();
        }
        
        switch (deviceType) {
            case MOUSE:
                inputDevice = new InputMouse();
                break;
            case KEYBOARD:
                inputDevice = new InputKeyboard();
                break;
            case GAMEPAD:
                inputDevice = new InputGamePad();
                break;
        }
    }
    
}
