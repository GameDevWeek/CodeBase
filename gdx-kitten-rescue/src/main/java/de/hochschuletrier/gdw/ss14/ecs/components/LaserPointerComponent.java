package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;


public class LaserPointerComponent implements Component{
    public static final float WATER_CONSUMPTION_SPEED = 0.1f; // percent/second
    public static final float WATER_REFILL_SPEED = 0.05f; // percent/second
    public Vector2 position;
    public float currentWaterlevel; // in percent: 1.0 = 100%
    public ToolState toolState;
    public boolean waterpistolIsUsed;
    
    public Pixmap laserCursor;
    public Pixmap waterpistolCursor;
    
    /*
     * !!! isVisible is now: toolState == ToolState.LASER !!!
     */
    
    public enum ToolState {
        LASER,
        WATERPISTOL
    }
    
    public LaserPointerComponent(Vector2 position){
        //isVisible = true;
        toolState = ToolState.LASER;
        waterpistolIsUsed = false;
        currentWaterlevel = 1.0f;
        this.position = position;
        laserCursor = new Pixmap(Gdx.files.internal("data/images/laser.png"));
        waterpistolCursor = new Pixmap(Gdx.files.internal("data/images/crosshair2.png"));
        Gdx.input.setCursorImage(laserCursor, laserCursor.getWidth() / 2, laserCursor.getHeight() / 2);
    }

}
