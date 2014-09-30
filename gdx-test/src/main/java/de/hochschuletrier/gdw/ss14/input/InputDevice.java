package de.hochschuletrier.gdw.ss14.input;

import java.util.HashMap;

public abstract class InputDevice {
    public static enum DeviceType {
        MOUSE("mouse"),
        KEYBOARD("keyboard"),
        GAMEPAD("gamepad");
        
        private String deviceName;

        private DeviceType(String deviceName) {
            this.deviceName = deviceName;
        }
        
        @Override
        public String toString() {
            return deviceName; 
        }
    };
    
    private HashMap<String, InputAction> buttonMap = new HashMap<>();
    
    
}
