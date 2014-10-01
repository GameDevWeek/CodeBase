package de.hochschuletrier.gdw.ss14.ecs.components;

public class DogStateComponent implements Component {

    public enum DogStateEnum {
        STANDING,
        CHASING,
        PATROLLING,
        JUMPING,
        ATTACKING
    }

}
