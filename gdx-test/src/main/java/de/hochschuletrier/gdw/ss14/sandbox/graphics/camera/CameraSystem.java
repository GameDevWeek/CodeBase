package de.hochschuletrier.gdw.ss14.sandbox.graphics.camera;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.ECSystem;

public class CameraSystem extends ECSystem {
    
  // Maximum distance of the cats center to the screen center in pixels
  private static final float MaxScreenCenterDistance = 200f;
  
  // Use this constant to model the exponential curve of the follow speed.
  // A higher number means that the camera will move slower at first.
  // If the max distance to the screen center is reached the camera will always move as fast as the cat does.
  private static final float FollowspeedCurvePower = 2.0f;
  
  private static final float CameraStartZoom = 2.0f;
    
  private TransformComponent followTransform;
  private LimitedSmoothCamera smoothCamera;
  
  /**
   * 
   * @param em          the entity manager
   * @param minBounds   minBounds of the screen
   * @param maxBounds   maxBounds of the screen
   * @param catEntity   the entity which belongs to the cat
   * @param priority    must be set so that the camera system will be updated right before the rendering system
   */
  public CameraSystem( EntityManager em, int catEntity, int priority ) {
      
      super(em, priority);      
      setCatEntity(catEntity);
      
      smoothCamera.setDestination(followTransform.position);
      smoothCamera.setZoom(CameraStartZoom);
      smoothCamera.updateForced();
  }
    
  @Override
  public void update( float delta ) {
      
      Vector2 camera2DPos = new Vector2(smoothCamera.getPosition().x, smoothCamera.getPosition().y);
      
      float centerDistance = followTransform.position.sub(camera2DPos).len();
      float followFactor = centerDistance / MaxScreenCenterDistance;
      
      followFactor = (float) Math.pow(followFactor, FollowspeedCurvePower);
      
      // Move camera towards the follow transform position by followFactor
      Vector2 newDest = camera2DPos.cpy();     
      newDest.add(followTransform.position.sub(newDest).scl(followFactor));
      
      smoothCamera.setDestination(newDest.x, newDest.y);
      
      smoothCamera.update(delta);
      smoothCamera.bind();
  }
  
  @Override
  public void render() {
      
  }
  
  public void setCatEntity( int catEntity ) {
      
      followTransform = entityManager.getComponent(catEntity, TransformComponent.class);
  }  
  
  public void setBounds( Vector2 minBounds, Vector2 maxBounds ) {
      
      smoothCamera.setBounds(minBounds.x, minBounds.y, maxBounds.x, maxBounds.y);
  }
}
