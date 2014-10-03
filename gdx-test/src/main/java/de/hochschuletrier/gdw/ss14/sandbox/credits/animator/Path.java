package de.hochschuletrier.gdw.ss14.sandbox.credits.animator;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class Path {

    public final ArrayList<Destination> destinations = new ArrayList();
    public final ArrayList<Animation> animations = new ArrayList();

    public void addAnimation(Animation destination) {
        animations.add(destination);
    }
    
    public void addDestination(Destination destination) {
        destinations.add(destination);
    }
}
