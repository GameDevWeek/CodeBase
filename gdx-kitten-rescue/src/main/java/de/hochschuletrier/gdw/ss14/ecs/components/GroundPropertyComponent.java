package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.CatStateEnum;
import de.hochschuletrier.gdw.ss14.states.GroundTypeState;
import de.hochschuletrier.gdw.ss14.states.JumpableState;

public class GroundPropertyComponent implements Component
{
    public GroundTypeState type;
    
    public GroundPropertyComponent(GroundTypeState type)
    {
        this.type = type;
    }
}
