package de.hochschuletrier.gdw.ws1415.game.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by oliver on 16.03.15.
 */

public enum Direction{
    RIGHT(0), UP(1), LEFT(2), DOWN(3); //, EAST(0), NORD(1), WEST(2), SOUTH(3);

    Direction(int v){
        this.value = v;
    }

    int value;

    public int toInt(){
        return value;
    }

    public Vector2 toVector2(){
        switch (this.value){
            case(0): return Vector2.X;
            case(1): return Vector2.Y;
            case(2): return Vector2.X.scl(-1);
            case(3): return Vector2.Y.scl(-1);
        }
        return null;
    }

    public static Direction fromInt(int i){
        switch (i){
            case(0): return RIGHT;
            case(1): return UP;
            case(2): return LEFT;
            case(3): return DOWN;
        }
        return null;
    };

    public static Direction fromVector2(Vector2 v){
        float i = v.dot(Vector2.X);
        float j = v.dot(Vector2.Y);
        if(i == 0 && j == 0) return null;
        if(Math.abs(i) > Math.abs(j))
            if(i > 0) return RIGHT;
            else return LEFT;
        else
            if(j > 0) return UP;
            else return DOWN;
    };

}