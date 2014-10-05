package de.hochschuletrier.gdw.ss14.ecs.components;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.physics.ICatStateListener;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;
import de.hochschuletrier.gdw.ss14.states.GroundTypeState;

public class CatPropertyComponent implements Component
{
    public static final int MAX_LIVES = 9;
    public static final float CATBOX_COOLDOWN = 2.0f;

    public Vector2 lastCheckPoint;
    public int amountLives;
    public boolean isHidden;
    public boolean canSeeLaserPointer;
    public boolean isAlive, atePositiveFood;
    private CatStateEnum state;

    public boolean isInfluenced = false;
    public float influencedToLaser = 1;

    public final float TIME_TILL_INFLUENCED = 2.5f;
    public float timeTillInfluencedTimer = 0;

    public final float TIME_TILL_JUMP = 0.5f;
    public float timeTillJumpTimer = 0;

    public final float PLAYTIME = 3f;
    public float playTimeTimer = 0;

    public boolean isCatBoxOnCooldown = false;
    public float catBoxCooldownTimer = CATBOX_COOLDOWN;

    public int idOfLastTouchedStair = -1;
    public boolean canChangeMap = true;
    
    public final float JUMP_IN_BOX = 1.2f;
    public float jumpInBoxTimer = 0;
    
    public final float JUMP_OUT_BOX = 1.2f;
    public float jumpOutBox = 0;

    public GroundTypeState groundWalking;
    
    public ArrayList<ICatStateListener> StateListener;

    public CatPropertyComponent()
    {
        lastCheckPoint = new Vector2();
        canSeeLaserPointer = true;
        amountLives = MAX_LIVES;
        isAlive = true;
        atePositiveFood = false;
        state = CatStateEnum.IDLE;
        isHidden = false;

        StateListener = new ArrayList<>();
    }

    public CatStateEnum getState(){
        return state;
    }

    public void setState(CatStateEnum newState){
        if(newState == state)return;
        StateListener.forEach((l)->l.stateChanged(state, newState));
        state = newState;
    }
}
