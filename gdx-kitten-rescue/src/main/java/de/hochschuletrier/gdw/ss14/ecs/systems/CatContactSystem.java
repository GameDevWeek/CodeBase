package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.ss14.ICollisionListener;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;

public class CatContactSystem extends ECSystem implements ICollisionListener{

    public CatContactSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void fireCollision(PhysixContact contact) {
        PhysixEntity owner = contact.getMyPhysixBody().getOwner();
        
        if(!(owner instanceof CatPhysicsComponent)) return;
        
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(contact.getClass());
        
        for(Integer c : compos){
            if(entityManager.getComponent(c, EnemyComponent.class) != null){
                //TODO: CAT DIE! 
                return;
            }
        }
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render() {}

    
    
}
