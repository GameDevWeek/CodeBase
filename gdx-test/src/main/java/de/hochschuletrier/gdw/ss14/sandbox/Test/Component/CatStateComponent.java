package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class CatStateComponent implements Component {
    public enum States {
        DISTRACTED, WALKING, STANDING, RUNNING, JUMPING
    };
}
