package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.*;
import de.hochschuletrier.gdw.ss14.states.*;

public class CatPropertyComponent implements Component
{
    public static final int MAX_LIVES = 9;

    public Vector2 lastCheckPoint;
    public int amountLives;
    public boolean canSeeLaserPointer;
    public boolean isAlive;
    public CatStateEnum state;
    public float jumpBuffer = 0;

    public CatPropertyComponent()
    {
        lastCheckPoint = new Vector2();
        canSeeLaserPointer = true;
        amountLives = MAX_LIVES;
        isAlive = true;
        state = CatStateEnum.IDLE;
    }
}
