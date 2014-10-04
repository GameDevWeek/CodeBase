package de.hochschuletrier.gdw.ss14.ecs.components;

import java.io.File;

public class ExitMapPropertyComponent implements Component{
    
    File nextMap;
    
    public ExitMapPropertyComponent(File nextMap){
        this.nextMap = nextMap;
    }
}
