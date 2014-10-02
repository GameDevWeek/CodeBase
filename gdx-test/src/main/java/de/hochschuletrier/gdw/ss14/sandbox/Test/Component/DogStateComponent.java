package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class DogStateComponent implements Component {

    public enum States {
        RUNNING, STANDING, WALKING, KILLING
    };
    public States state = States.STANDING;
}
