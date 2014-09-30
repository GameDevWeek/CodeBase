package de.hochschuletrier.gdw.ss14.sandbox.graphics;

import com.badlogic.gdx.math.Vector2;

public interface IRenderable {
    
    /**
     * @return returns the position of the object as a 2D vector.
     */
    public Vector2 getPosition();
    
    /**
     * @return returns the rotation of the object in degrees (-360 to 360)
     *  Looking to the top equals a rotation of 0 or 360.
     *  Looking to the left equals a rotation of 90.
     *  Looking to the right equals a rotation of -90.
     */
    public float getRotation();
    
    /**
     * @return returns the scale of the object. 
     *  Usually 1.0f.
     */
    public float getScale();
    
    /**
     * @return returns the type of the object to render (a.e.: a cat, a dog, the laserpointer, ...)
     */
    public RenderableObjectType getRenderableObjectType();
    
    /**
     * @return returns the animation state the renderable object is in.
     *  This should be an integer casted from the object-corresponding enum (a.e.: CatAnimationState, DogAnimationState, ...)
     */
    public int getAnimationState();
}
