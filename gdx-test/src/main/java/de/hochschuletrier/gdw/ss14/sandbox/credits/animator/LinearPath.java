package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Path;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class LinearPath implements Path<Vector2> {

    public final ArrayList<Destination> destinations;
    
    public LinearPath(ArrayList<Destination> destinations) {
        this.destinations = destinations;
        
        // Make sure all times are set correctly
        Vector2 temp = new Vector2();
        float startTime = 0;
        int index = 0;
        int lastIndex = destinations.size()-1;
        for(Destination dest: destinations) {
            if(index < lastIndex) {
                Destination next = destinations.get(index+1);
                dest.moveTime = temp.set(next).sub(dest).len() / dest.speed;
            }
            dest.startTime = startTime;
            startTime +=  dest.moveTime;
            index++;
        }
    }
    
    private int getStartIndex(float t) {
        int i = -1;
        for(Destination dest: destinations) {
            if(dest.startTime > t) {
                break;
            }
            i++;
        }
        return Math.max(0, i);
    }

    @Override
    public Vector2 derivativeAt(Vector2 out, float t) {
        int index = getStartIndex(t);
        if(index == destinations.size()-1)
            index--;
        Vector2 start = destinations.get(index);
        Vector2 next = destinations.get(index+1);
        return out.set(next).sub(start).nor();
    }

    @Override
    public Vector2 valueAt(Vector2 out, float t) {
        int index = getStartIndex(t);
        Destination start = destinations.get(index);
        
        if(index == destinations.size()-1) {
            return out.set(start);
        }
        
        Vector2 next = destinations.get(index+1);
        return out.set(next).sub(start).scl((t - start.startTime)/start.moveTime).add(start);
    }

    @Override
    public float approximate(Vector2 v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float locate(Vector2 v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float approxLength(int samples) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
