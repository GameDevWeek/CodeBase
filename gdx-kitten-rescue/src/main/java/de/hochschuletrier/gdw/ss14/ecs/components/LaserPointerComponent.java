package de.hochschuletrier.gdw.ss14.ecs.components;


public class LaserPointerComponent implements Component{
    
    public enum InputState {
        KEYBOARD,
        MOUSE,
        GAMEPAD,
    }
    
    public InputState input;
    
    public LaserPointerComponent(){
        input = InputState.MOUSE;
    }

}
