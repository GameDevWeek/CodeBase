package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;

/**
 * @author David Neubauer
 */
public class AnimationComponent implements Component {

    /**
     * Array of all animations like running, jumping ...
     */
    public AnimationExtended animation[];
    
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
     * True if the animation is finished or played one (if looped)
     */
    public boolean isFinished;
}
