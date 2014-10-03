package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.physics.box2d.Fixture;
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
import de.hochschuletrier.gdw.ss14.physics.ICollisionListener;
import de.hochschuletrier.gdw.ss14.physics.RayCastPhysics;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;

public class CatContactSystem extends ECSystem implements ICollisionListener{

    private static final Logger logger = LoggerFactory.getLogger(CatContactSystem.class);

    private PhysixManager phyManager;
    private RayCastPhysics rcp;

    public CatContactSystem(EntityManager entityManager, PhysixManager physicsManager) {
        super(entityManager);
        phyManager = physicsManager;
    }

    @Override
    public void fireBeginnCollision(PhysixContact contact) {
        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();

        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
        PhysixEntity other = contact.getOtherPhysixBody().getOwner();

        if(other instanceof RectPhysicsComponent){
            logger.debug("cat collides with dog ... or another cat");


        }else if(other instanceof CatPhysicsComponent){
            logger.debug("cat collides with sight-cone");
            phyManager.getWorld().rayCast(rcp, other.getPosition(), owner.getPosition());
            if(rcp.m_hit && rcp.m_fraction <= ((CatPhysicsComponent)other).coneRadius){
                for(Fixture f : other.physicsBody.getFixtureList()){
                    if(rcp.m_fixture == f){
                        EnemyComponent.seeCat = true;
                        logger.debug("Katze sichtbar für Hund");
                    }
                }
                
            }else{
                //dog sees cat not
            }
            rcp.reset();
        }else if(other instanceof WoolPhysicsComponent){
            ((WoolPhysicsComponent) other).isSeen = true;
            Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PlayerComponent.class);
            CatPropertyComponent player = entityManager.getComponent(compos.get(0), CatPropertyComponent.class);
            player.isInfluenced = true;
            logger.debug("WOOOOOOOOOOOOOOOLL");
        }else if(other instanceof JumpablePhysicsComponent){
            Array<Integer> compos = entityManager.getAllEntitiesWithComponents(JumpablePropertyComponent.class);
            for (Integer p : compos) {
                JumpablePropertyComponent property = entityManager.getComponent(p, JumpablePropertyComponent.class);
                PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                if(puddlecompo == other)
                {
                    if(property.type == JumpableState.deadzone)
                    {
                        boolean isCatInZone = false;
                        if (contact.getMyFixture().getUserData() == null) return;
                        if (contact.getMyFixture().getUserData().equals("masscenter"))
                        {
                            isCatInZone = true;
                        }
                        if (isCatInZone)
                        {
                            // cat fall down
                            Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);

                            if (entities.size > 0)
                            {
                                int player = entities.first();
                                CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);

                                //catPropertyComponent.isAlive = false;
                                catPropertyComponent.setState(CatStateEnum.FALL);
                            }

                        }
                    } // end dead zone check
                    else if(property.type == JumpableState.waterpuddle || property.type == JumpableState.bloodpuddle)
                    {
                        // TODO: DRY!
                        boolean isCatInZone = false;
                        if (contact.getMyFixture().getUserData() == null) return;
                        if (contact.getMyFixture().getUserData().equals("masscenter"))
                        {
                            isCatInZone = true;
                        }
                        if (isCatInZone)
                        {
                            // cat fall down
                            Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);

                            if (entities.size > 0)
                            {
                                int player = entities.first();
                                CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);

                                catPropertyComponent.isAlive = false;
                            }
                        }

                    }
                } // end if other
                
            }
            
        }else if(other == null){
            if(!(o instanceof String)) return;
        }
        else if(other instanceof CatBoxPhysicsComponent)
        {
            Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class, RenderComponent.class);

            if(entities.size > 0)
            {
                int player = entities.first();

                RenderComponent renderComponent = entityManager.getComponent(player, RenderComponent.class);
                CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);

                if(!catPropertyComponent.isCatBoxOnCooldown)
                {
                    catPropertyComponent.isCatBoxOnCooldown = true;
                    catPropertyComponent.catBoxCooldownTimer = catPropertyComponent.CATBOX_COOLDOWN;
                    entityManager.removeComponent(player, renderComponent);

                    //catPropertyComponent.setState(CatStateEnum.HIDDEN);

                    catPropertyComponent.isHidden = true;
                }
                else
                {
                    return;
                }
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

    @Override
    public void fireEndCollision(PhysixContact contact) {
        // TODO Auto-generated method stub
        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();
        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
        PhysixEntity other = contact.getOtherPhysixBody().getOwner();
        
        if(other instanceof WoolPhysicsComponent){
            ((WoolPhysicsComponent) other).isSeen = false;
            Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PlayerComponent.class);
            CatPropertyComponent player = entityManager.getComponent(compos.get(0), CatPropertyComponent.class);
            player.isInfluenced = false;
            
        }
        
        if(other instanceof CatPhysicsComponent){
            EnemyComponent.seeCat = false;
        }
        
    }
}
