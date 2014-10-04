package de.hochschuletrier.gdw.ss14.ecs.components;

import java.util.HashMap;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

/**
 * @author David Neubauer
 */
public class AnimationComponent implements Component {
    
    /**
     * Array of all animations like running, jumping ...
     */
    public HashMap<Integer, AnimationExtended> animation = new HashMap<>();
    
    /**
     * In sync with entity state like CatStateEnum.
     * Do not change this if you want a jumping cat, 
     * change the state of the cat!
     */
    public int currentAnimationState;
    
    /**
     * Time that has passed since the beginning of the animation.
     */
    public float animationTime;
    
    /**
     * True if the animation is finished.
     * False is the animationMode isn't NORMAL or if the animation isn't finished.
     */
    public boolean isFinished;
    
    /**
     * Set this if the animation should change the speed in relation to the speed of the object.
     * A good default value is around 50 and 100;
     * Lower values means quicker animations.
     */
    public HashMap<Integer, Float> speedUpFactor = new HashMap<>();
}
