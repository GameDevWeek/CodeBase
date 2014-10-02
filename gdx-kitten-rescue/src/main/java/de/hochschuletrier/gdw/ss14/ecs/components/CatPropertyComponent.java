package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

public class CatPropertyComponent implements Component
{
    public static final int MAX_LIVES = 9;
    
    public Vector2 lastCheckPoint;
    public int amountLives;
    public boolean canSeeLaserPointer;
    public boolean isAlive, atePositiveFood;
    public CatStateEnum state;
    public float timeTillJump = 0;
    public float timeWhileJump = 0;

    public CatPropertyComponent()
    {
        lastCheckPoint = new Vector2();
        canSeeLaserPointer = true;
        amountLives = MAX_LIVES;
        isAlive = true;
        atePositiveFood = false;
        state = CatStateEnum.IDLE;
    }
}
