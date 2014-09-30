package de.hochschuletrier.gdw.ss14.input;


public class InputManager {
    private static InputManager instance;
    
    public static InputManager getInstance () {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }
    
    private InputManager(){
    }
    
    private InputDevice inputDevice = new InputMouse();
    
    public void addGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void removeGameInputAdapter(GameInputAdapter gia) {
        inputDevice.addGameInputAdapter(gia);
    }
    
    public void update() {
        
    }
    
    
}
