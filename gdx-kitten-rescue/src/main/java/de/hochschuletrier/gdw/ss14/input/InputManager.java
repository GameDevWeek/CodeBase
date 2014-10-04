package de.hochschuletrier.gdw.ss14.input;

import java.util.LinkedList;

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
    protected LinkedList<GameInputAdapter> listener = new LinkedList<>();
    
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
        listener.add(gia);
        addListenerToDevice(gia);
    }
    
    public void removeGameInputAdapter(GameInputAdapter gia) {
        listener.add(gia);
        removeListenerFromDevice(gia);
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
        if (inputDevice != null) {
            inputDevice.registerProcessor();
        }
    }
    
    public void unregisterProcessor() {
        if (inputDevice != null) {
            inputDevice.unregisterProcessor();
        }
    }
    
    public void changeInputDevice(DeviceType deviceType) {
        boolean wasRegistred = false;
        if (inputDevice != null) {
            wasRegistred = inputDevice.isRegistred;
            unregisterProcessor();
            removeAllListenerFromDevice();
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
        
        addAllListenerToDevice();
        writeSettings(inputDevice.devicType);
        
        if (wasRegistred) {
            inputDevice.registerProcessor();
        }
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
    
    private void addListenerToDevice(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    private void addAllListenerToDevice() {
        for (GameInputAdapter gia : listener) {
            addListenerToDevice(gia);
        }
    }
    
    private void removeListenerFromDevice(GameInputAdapter gia) {
        inputDevice.removeGameInputAdapter(gia);
    }
    
    private void removeAllListenerFromDevice() {
        for (GameInputAdapter gia : listener) {
            removeListenerFromDevice(gia);
        }
    }
    
}
