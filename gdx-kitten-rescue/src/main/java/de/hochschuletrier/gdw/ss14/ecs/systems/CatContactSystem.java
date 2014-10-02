package de.hochschuletrier.gdw.ss14.ecs.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.ss14.ecs.ICollisionListener;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.ConePhysicsComponent;

public class CatContactSystem extends ECSystem implements ICollisionListener{

    private static final Logger logger = LoggerFactory.getLogger(MovementSystem.class);
    
    public CatContactSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void fireCollision(PhysixContact contact) {
        PhysixEntity owner = contact.getMyPhysixBody().getOwner();
        
        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
        PhysixEntity other = contact.getOtherPhysixBody().getOwner();
        
        if(other instanceof CatPhysicsComponent){
            logger.debug("cat collides with dog ... or another cat");
            
            
        }else if(other instanceof ConePhysicsComponent){
            logger.debug("cat collides with sight-cone");
            //TODO: raycast from
            //other.getPosition();
            // to
            //owner.getPosition();
            // with maximum ((ConePhysicsComponent)other).mRadius;
            // if hit → dog sees cat, else not
            
        }else if(other == null){
            if(!(o instanceof String)) return;
            String s = (String)o;
            if(s.equals("deadzone")){
                //if(contact.getOtherPhysixBody().)
                boolean isCatInZone = false;
                for(Fixture f : contact.getOtherPhysixBody().getFixtureList()){
                    isCatInZone |= f.testPoint(owner.getPosition());
                }
                if(isCatInZone){
                    // cat fall down
                }
            }
        }
        
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render() {}
}
