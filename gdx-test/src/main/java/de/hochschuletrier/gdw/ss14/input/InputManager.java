package de.hochschuletrier.gdw.ss14.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

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
        switch (GameSettings.getInstance().getInputDevice()) {
        case MOUSE:
            System.out.println("Maus");
            break;
        case KEYBOARD:
            System.out.println("Keyboard");
            break;
        case GAMEPAD:
            System.out.println("Gamepad");
            break;
        }
    }
    
    private InputDevice inputDevice = new InputKeyboard();
    
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
        Main.inputMultiplexer.addProcessor(instance.inputDevice);
    }
    
}
