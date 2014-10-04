package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.ss14.states.DogStateEnum;

public class DogPropertyComponent implements Component{

    public DogStateEnum state;
    public boolean dogIsChasing;

    public DogPropertyComponent(){
        state = DogStateEnum.SITTING;
        dogIsChasing = true;
    }
}
