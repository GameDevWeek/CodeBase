package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.CatStateEnum;
import de.hochschuletrier.gdw.ss14.states.JumpableState;

public class JumpablePropertyComponent implements Component
{
    JumpableState type;
    
    public JumpablePropertyComponent(JumpableState type)
    {
        this.type = type;
    }
}
