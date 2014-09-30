package de.hochschuletrier.gdw.ss14.sandbox.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.PhysicsComponent;

public class CameraSystem extends ECSystem {
        
  private LimitedSmoothCamera smoothCamera;
  
  /**
   * 
   * @param em          the entity manager
   * @param minBounds   minBounds of the screen
   * @param maxBounds   maxBounds of the screen
   * @param catEntity   the entity which belongs to the cat
   * @param priority    must be set so that the camera system will be updated right before the rendering system
   */
  public CameraSystem( EntityManager em, int priority ) {
      
      super(em, priority);      
  }
    
  @Override
  public void update( float delta ) {
      
      Array<Integer> entityArray = entityManager.getAllEntitiesWithComponents(CameraComponent.class, PhysicsComponent.class);

      for (Integer entity : entityArray) {
          
          CameraComponent camComp = entityManager.getComponent(entity, CameraComponent.class);
          Vector2 followTransformPosition = entityManager.getComponent(entity, PhysicsComponent.class).getPosition();         
          Vector2 camera2DPos = new Vector2(smoothCamera.getPosition().x, smoothCamera.getPosition().y);
          
          float centerDistance = followTransformPosition.sub(camera2DPos).len();
          float followFactor = centerDistance / camComp.maxScreenCenterDistance;
          
          followFactor = (float) Math.pow(followFactor, camComp.followspeedCurvePower);
          
          // Move camera towards the follow transform position by followFactor
          Vector2 newDest = camera2DPos.cpy();     
          newDest.add(followTransformPosition.sub(newDest).scl(followFactor));
          
          if ((camComp.minBound != null) && (camComp.maxBound != null))
              smoothCamera.setBounds(camComp.minBound.x, camComp.minBound.y, camComp.maxBound.x, camComp.maxBound.y);
          
          smoothCamera.setDestination(newDest.x, newDest.y);     
          smoothCamera.setZoom(camComp.cameraZoom);
          smoothCamera.update(delta);
          smoothCamera.bind();
      }
  }
  
  @Override
  public void render() {
      
  }
  
  /*public void setBounds( Vector2 minBounds, Vector2 maxBounds ) {
      
      smoothCamera.setBounds(minBounds.x, minBounds.y, maxBounds.x, maxBounds.y);
  }*/
}
