package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.*;

public class InputComponent implements Component
{

    public Vector2 whereToGo;

    public InputComponent()
    {
        whereToGo = new Vector2(0.0f, 0.0f);
    }

}
