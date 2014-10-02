package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ss14.Main;

public class CameraComponent implements Component 
{    
    public LimitedSmoothCamera smoothCamera;
    
    // Maximum distance of the cats center to the screen center in pixels
    public float maxScreenCenterDistance = 75f;
    
    // The time in seconds the camera needs to go from the max distance to the center of the cat.
    // This time will be further modified by followspeedCurvePower
    public float movetimeFromMaxDistanceToCenter = 0.5f;
    
    // Use this constant to model the exponential curve of the follow speed.
    // A higher number means that the camera will move slower at first.
    // If the max distance to the screen center is reached the camera will always move as fast as the cat does.
    public float followspeedCurvePower = 1.0f;
    
    public float cameraZoom = 1.0f;
    
    // The bounds of the level in pixels
    public Vector2 minBound = null;
    public Vector2 maxBound = null;
    
    public CameraComponent() {
        
        smoothCamera = new LimitedSmoothCamera();
        Main.getInstance().addScreenListener(smoothCamera);
        
        smoothCamera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        smoothCamera.updateForced();
    }
}
