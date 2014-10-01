package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationWithVariableFrameTime;

/**
 * @author David Neubauer
 */
public class AnimationComponent implements Component {

    /**
     * Array of all animations like running, jumping ...
     */
    public AnimationWithVariableFrameTime animation[];
    
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
}
