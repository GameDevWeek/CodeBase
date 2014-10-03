package de.hochschuletrier.gdw.ss14.ecs.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.DogStateEnum;

public class DogPropertyComponent implements Component{

    public DogStateEnum state;
    public ArrayList<Vector2> patrolspots;

    public DogPropertyComponent(ArrayList<Vector2> patrolspots){
        state = DogStateEnum.SITTING;
        this.patrolspots = patrolspots;
    }
}
