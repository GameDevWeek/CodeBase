package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.ss14.ecs.ICollisionListener;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;

public class CatContactSystem extends ECSystem implements ICollisionListener{

    private static final Logger logger = LoggerFactory.getLogger(CatContactSystem.class);

    public CatContactSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void fireCollision(PhysixContact contact) {
        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();
        
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
                boolean isCatInZone = false;
                if(contact.getMyFixture().getUserData() == null) return;
                if(contact.getMyFixture().getUserData().equals("masscenter")){
                    isCatInZone = true;
                }
                if(isCatInZone){
                    // cat fall down
                    int player = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class).first();

                    CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);
                    PhysicsComponent physicsComponent = entityManager.getComponent(player, PhysicsComponent.class);

                    catPropertyComponent.isAlive = false;
                }
            }
        }
        
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render() {}
}
