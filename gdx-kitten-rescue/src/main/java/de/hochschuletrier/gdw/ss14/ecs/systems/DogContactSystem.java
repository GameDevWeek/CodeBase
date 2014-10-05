package de.hochschuletrier.gdw.ss14.ecs.systems;

import java.util.List;



import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.Component;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.physics.ICollisionListener;
import de.hochschuletrier.gdw.ss14.states.DogStateEnum;
import de.hochschuletrier.gdw.ss14.states.JumpableState;

public class DogContactSystem extends ECSystem implements ICollisionListener{

    EntityManager entityManager;
    PhysixManager phyManager;
    
    public DogContactSystem(EntityManager entityManager, PhysixManager phyManager) {
        super(entityManager);
        this.entityManager = entityManager;
        this.phyManager = phyManager;
    }

    @Override
    public void fireBeginnCollision(PhysixContact contact) {
         PhysixBody owner = contact.getMyPhysixBody();
         PhysixEntity other = contact.getOtherPhysixBody().getOwner();
         
         if(other instanceof JumpablePhysicsComponent){
             
             
             Array<Integer> physicEntities = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
             Integer myEntity = null, otherEntity = null;
             PhysicsComponent myPhysic = null, otherPhysic = null;
             for (Integer i : physicEntities)
             {
                 PhysicsComponent tmp = entityManager.getComponent(i, PhysicsComponent.class);
                 if (tmp.physicsBody == contact.getMyPhysixBody())
                 {
                     myPhysic = tmp;
                     myEntity = i;
                 }
                 if (tmp.physicsBody == contact.getOtherPhysixBody())
                 {
                     otherEntity = i;
                     otherPhysic = tmp;
                 }
             }
             
             Component component = entityManager.getComponent(otherEntity, JumpablePropertyComponent.class);
             Component dogcomponent = entityManager.getComponent(myEntity, DogPropertyComponent.class);
             if(component == null)
                 return;
             switch (((JumpablePropertyComponent) component).type)
             {
                 case deadzone:
                     
                     if(((DogPropertyComponent) dogcomponent).state != DogStateEnum.FALLING)
                     entityManager.deletePhysicEntity(myEntity);
                     ((DogPropertyComponent) dogcomponent).setState(DogStateEnum.FALLING);
                     
                     break;
            default:
                break;
             }
             
         }
        
    }

    @Override
    public void fireEndCollision(PhysixContact contact) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }
    
}
