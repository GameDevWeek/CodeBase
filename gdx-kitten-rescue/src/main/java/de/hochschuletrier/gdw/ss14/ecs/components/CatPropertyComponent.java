package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

public class CatPropertyComponent implements Component
{
    public static final int MAX_LIVES = 9;
    
    public Vector2 lastCheckPoint;
    public int amountLives;
    public boolean isHidden;
    public boolean canSeeLaserPointer;
    public boolean isAlive, atePositiveFood;
    public CatStateEnum state;
    
    public float influencedToLaser = 1;
    
    public final float TIME_TILL_INFLUENCED = 2.5f;
    public float timeTillInfluencedTimer = 0;
    
    public final float TIME_TILL_JUMP = 1f;
    public float timeTillJumpTimer = 0;
    
    public final float TIME_WHILE_JUMP = 2f;
    public float timeWhileJumpTimer = 0;
    
    public final float PLAYTIME = 1.5f;
    public float playTimeTimer = 0;
    
    public CatPropertyComponent()
    {
        lastCheckPoint = new Vector2();
        canSeeLaserPointer = true;
        amountLives = MAX_LIVES;
        isAlive = true;
        atePositiveFood = false;
        state = CatStateEnum.IDLE;
        isHidden = false;
    }
}
