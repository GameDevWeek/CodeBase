package de.hochschuletrier.gdw.ss14.sandbox.ecs.components;

import com.badlogic.gdx.math.*;

/**
 * Created by Dani on 30.09.2014.
 */
public class TestInputComponent implements Component
{
    public Vector2 target;

    public TestInputComponent()
    {
        this.target = new Vector2();
    }
}
