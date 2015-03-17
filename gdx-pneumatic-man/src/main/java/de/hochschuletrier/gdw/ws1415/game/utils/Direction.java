package de.hochschuletrier.gdw.ws1415.game.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by oliver on 16.03.15.
 */
public class Direction {

    private int direction;

    Direction(int dir){ direction = dir; }
    Direction(DirectionType dir){ direction = dir.ordinal(); }

    public static Vector2 DirectionToVector2(Direction d){
        return new Vector2(
                (float)Math.cos(0.5f * Math.PI * d.direction),
                (float)Math.sin(0.5f * Math.PI * d.direction)
        );
    }

    public static int DirectionToInt(Direction d){
        return d.direction;
    }

    public static DirectionType DirectionToType(Direction d){
        return DirectionType.values()[d.direction];
    }

    public enum DirectionType{
        Right, Up, Left, Down
    }
}
