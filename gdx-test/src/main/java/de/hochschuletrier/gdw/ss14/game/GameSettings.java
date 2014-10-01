package de.hochschuletrier.gdw.ss14.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.commons.jackson.JacksonWriter;
import de.hochschuletrier.gdw.ss14.input.InputDevice;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;

public class GameSettings {
    private static final Logger logger = LoggerFactory.getLogger(GameSettings.class);
    private static final String fileName = "src/main/resources/data/json/settings.json";
    private static GameSettings instance;
    
    private Float volume = 1.0f;
    private Boolean fullscreen = false;
    private InputDevice.DeviceType inputDevice = DeviceType.MOUSE;
    
    public GameSettings() {
    }
     
    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public InputDevice.DeviceType getInputDevice() {
        return inputDevice;
    }

    public void setInputDevice(InputDevice.DeviceType inputDevice) {
        this.inputDevice = inputDevice;
    }

    public void write() {
        try {
            JacksonWriter.write(fileName, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static GameSettings getInstance() {
        if (instance == null) {
            instance = GameSettings.read();
            if (instance == null) {
                instance = new GameSettings();
            }
        }
        return instance;
    }
    
    public static GameSettings read() {
        GameSettings o = null;
        try {
            o = JacksonReader.read(fileName, GameSettings.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
}
