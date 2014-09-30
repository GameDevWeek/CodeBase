package de.hochschuletrier.gdw.ss14.sandbox.Test.Component;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class CatPropertyComponent implements Component {

    public int amountLives;
    public boolean isAlive;
    public int thirstBar;

    public CatPropertyComponent() {
        amountLives = 9;
        isAlive = true;

    }
}
