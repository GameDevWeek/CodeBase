package de.hochschuletrier.gdw.ss14.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.io.InputDecorator;

import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ss14.input.InputDevice;

public class GameSettings {
    private static final Logger logger = LoggerFactory.getLogger(GameSettings.class);
    public static GameSettings instance;
    
    private float volume;
    private boolean fullscreen;
    private InputDevice.DeviceType inputDevice;
    
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
        
    }
    
    public static GameSettings getInstance() {
        if (instance == null) {
            GameSettings.read();
        }
        return instance;
    }
    
    public static GameSettings read() {
        try {
            instance = JacksonReader.read("resources/data/json/settings.json", GameSettings.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
