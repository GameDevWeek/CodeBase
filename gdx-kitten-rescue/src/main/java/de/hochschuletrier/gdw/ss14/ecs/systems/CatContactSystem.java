package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.states.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.ICollisionListener;
import de.hochschuletrier.gdw.ss14.physics.RayCastPhysics;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.ConePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;

public class CatContactSystem extends ECSystem implements ICollisionListener{

    private static final Logger logger = LoggerFactory.getLogger(CatContactSystem.class);

    private PhysixManager phyManager;
    private RayCastPhysics rcpc;

    public CatContactSystem(EntityManager entityManager, PhysixManager physicsManager) {
        super(entityManager);
        phyManager = physicsManager;
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
            phyManager.getWorld().rayCast(rcpc, other.getPosition(), owner.getPosition());
            if(rcpc.m_hit && rcpc.m_fraction <= ((ConePhysicsComponent)other).mRadius){
                //dog sees cat
                logger.debug("Katze sichtbar für Hund");
            }else{
                //dog sees cat not
            }
            rcpc.reset();
        }else if(other instanceof WoolPhysicsComponent){
        
        }else if(other instanceof JumpablePhysicsComponent){
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

                    catPropertyComponent.isAlive = false;
                    //catPropertyComponent.state = CatStateEnum.FALL;
                }
            }
        }
        else if(other instanceof CatBoxPhysicsComponent)
        {
            Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, CatPropertyComponent.class, RenderComponent.class);

            if(entities.size > 0)
            {
                int player = entities.first();

                RenderComponent renderComponent = entityManager.getComponent(player, RenderComponent.class);
                CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);

                entityManager.removeComponent(player, renderComponent);

                catPropertyComponent.isHidden = true;
            }

            Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);

            for (Integer entity : lasers)
            {
                LaserPointerComponent laserPointerComponent = entityManager.getComponent(entity, LaserPointerComponent.class);

                laserPointerComponent.isVisible = false;
            }

        }

    }

    @Override
    public void update(float delta) {}

    @Override
    public void render() {}
}
