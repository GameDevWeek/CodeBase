package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.*;

/**
 * Created by Daniel Dreher on 03.10.2014.
 */
public class JumpDataComponent implements Component
{
    public float maxJumpTime = 1.0f;
    public float currentJumpTime = 0.0f;
    public float jumpVelocity = 200.0f;
    public Vector2 jumpDirection;

    public JumpDataComponent()
    {
        jumpDirection = new Vector2();
    }
}
