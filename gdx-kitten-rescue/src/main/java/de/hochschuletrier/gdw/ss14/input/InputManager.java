package de.hochschuletrier.gdw.ss14.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.GdxRuntimeException;

import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;
import de.hochschuletrier.gdw.ss14.preferences.PreferenceKeys;

public class InputManager {
    public static final DeviceType STANDARD_DEVICETYPE = DeviceType.MOUSE;
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
    
    public DeviceType getCurrentDeviceTye() {
        if (this.inputDevice != null) {
            throw new GdxRuntimeException("No device initialized yet!");
        }
        return this.inputDevice.getDeviceType();
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
        
        instance.changeInputDevice(InputManager.readSettings());
        
        InputManager.readSettings();
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
        
        writeSettings(inputDevice.devicType);
    }
    
    public static DeviceType readSettings() {
        DeviceType ret;
        try {
            ret = DeviceType.valueOf(Main.getInstance().gamePreferences.getString(PreferenceKeys.inputDevice, STANDARD_DEVICETYPE.name()));
        } catch (IllegalArgumentException ex) {
            // Wrong Typename in Settings, go on with STANDARD_DEVICETYPE
            ret = STANDARD_DEVICETYPE;
        }
        return ret;
    }
    
    public static void writeSettings(DeviceType deviceType) {
        Main.getInstance().gamePreferences.putString(PreferenceKeys.inputDevice, deviceType.name());
    }
    
}
