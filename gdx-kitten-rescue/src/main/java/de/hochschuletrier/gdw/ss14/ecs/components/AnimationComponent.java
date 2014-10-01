package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.commons.gdx.AnimationFrameTime;

/**
 *
 * @author David Neubauer
 */
public class AnimationComponent implements Component {
    public AnimationFrameTime animation[];
    public int actualAnimationState; // in sync with entity state like CatStateEnum
    public float animationTime;
}
