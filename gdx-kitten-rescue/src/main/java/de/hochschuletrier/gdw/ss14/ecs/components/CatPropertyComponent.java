package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.Component;

public class CatPropertyComponent implements Component {

    public int amountLives;
    public int thurstBar;
    public boolean isAlive;

    public CatPropertyComponent() {
        amountLives = 9;
        isAlive = true;
    }

}
