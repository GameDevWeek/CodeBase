package de.hochschuletrier.gdw.ss14.input.infos;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import de.hochschuletrier.gdw.commons.jackson.JacksonList;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.jackson.JacksonWriter;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;

public class InputSettings {
    private static final String fileName = "src/main/resources/data/json/ctrls.json";
    private static InputSettings instance;
    
    public static InputSettings getInstance() {
        if (instance == null) {
            instance = new InputSettings();
            instance.read();
        }
        return instance;
    }
    
    @JacksonList(InputInfo.class)
    public HashMap<String, InputInfo> inputInfos = new HashMap<>();
    
    public InputSettings() {
        createStandardInputInfos();
    }
    
    private void createStandardInputInfos() {
        InputInfo i;
        
        // Mouse
        i = new InputInfo();
        i.type = DeviceType.MOUSE;
        i.NAME = DeviceType.MOUSE.name();
        i.SHOOT = "RIGHT";
        i.TOGGLE_LASER = "LEFT";
        this.inputInfos.put(i.NAME, i);
        
        // Keyboard
        i = new InputInfo();
        i.type = DeviceType.KEYBOARD;
        i.NAME = DeviceType.KEYBOARD.name();
        i.MOVE_UP = "W";
        i.MOVE_DOWN = "S";
        i.MOVE_LEFT = "A";
        i.MOVE_RIGHT = "D";
        i.SHOOT = "L";
        i.TOGGLE_LASER = "K";
        this.inputInfos.put(i.NAME, i);
        
        // GamePads
        if (Controllers.getControllers().size > 0) {
            Controller c = Controllers.getControllers().first();
            i = new InputInfo();
            i.type = DeviceType.GAMEPAD;
            i.NAME = c.getName();
            i.SHOOT = "2";
            i.TOGGLE_LASER = "0";
            this.inputInfos.put(i.NAME, i);
        }
        
    }
    
    public InputInfo getInputInfo(DeviceType type) {
        for (InputInfo info : this.inputInfos.values()) {
            if (info.type == type) {
                return info;
            }
        }
        return null;
    }
    
    public InputInfo getInputInfo(DeviceType type, String deviceName) {
        // Achtung: Nur für GamePads gedacht
        if (type == DeviceType.GAMEPAD && !deviceName.isEmpty()) {
            for (InputInfo info : this.inputInfos.values()) {
                if (info.type == type && info.NAME.equalsIgnoreCase(deviceName)) {
                    return info;
                }
            }
        }
        return null;
    }
    
    public void write() {
        try {
            JacksonWriter.write(fileName, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void read() {
        InputSettings o = null;
        try {
            o = JacksonReader.read(fileName, InputSettings.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (o != null) {
            for (Entry<String, InputInfo> entry : o.inputInfos.entrySet()) {
                this.inputInfos.remove(entry.getKey());
                this.inputInfos.put(entry.getKey(), entry.getValue());
            }
        }
        
    }
}
