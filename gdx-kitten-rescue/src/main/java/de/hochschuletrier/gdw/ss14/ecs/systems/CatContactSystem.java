package de.hochschuletrier.gdw.ss14.ecs.systems;

import javax.security.auth.callback.Callback;
import javax.swing.text.html.parser.Entity;

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

        /////////////
        // get all neccessary information
        Array<Integer> physicEntities = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
        Integer myEntity = null, otherEntity = null;
        PhysicsComponent otherPhysic = null;
        for(Integer i : physicEntities){
            PhysicsComponent tmp = entityManager.getComponent(i, PhysicsComponent.class);
            if(tmp.physicsBody == contact.getMyPhysixBody()){ myEntity = i;}
            if(tmp.physicsBody == contact.getOtherPhysixBody()){ otherEntity = i; otherPhysic = tmp; }
        }
        // checks if the center of the cat is collided
        boolean isCatInZone = false, mySightCone = false, otherSightCone = false;
        if(contact.getMyFixture().getUserData() != null){
            if(contact.getMyFixture().getUserData().equals("masscenter")){
                isCatInZone = true;
            }else if(contact.getMyFixture().getUserData().equals("sightcone")){
                mySightCone = true;
            }
        }
        if(contact.getOtherFixture().getUserData() != null){
            if(contact.getOtherFixture().getUserData().equals("sightcone")){
                otherSightCone = true;
            }
        }
            
        //////////
        // if something wierd happens, one of these is null then dont go on
        if(myEntity == null || otherEntity == null || otherPhysic == null) return;
        
        Component c = null, d = null;
        if( (c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null ){
            /*other → is enemy */
            if(otherPhysic instanceof CatPhysicsComponent){
                if(mySightCone) return; // katze sieht hund/ oder sichtfelder berühren sich → egal
                // kollidiert mit hund (oder anderer katze)
                if(otherSightCone){
                    // katzenkörper berührt hunde sichtfeld
                    
                }else{
                    // katzenkörper berührt hundekörper
                    
                }
            }
            
        }else if( (c = entityManager.getComponent(otherEntity, JumpablePropertyComponent.class) ) != null ){
            /*other → is jumpable object */
            switch(((JumpablePropertyComponent)c).type){
            case deadzone:
                if(! isCatInZone) break;
                if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                    ((CatPropertyComponent)d).setState(CatStateEnum.FALL);  
            break;
            default:break;
            }
        }else if( (c = entityManager.getComponent(otherEntity, GroundPropertyComponent.class) ) != null ){
            /* other → is groundobject */
            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                ((CatPropertyComponent)d).groundWalking = ((GroundPropertyComponent)c).type;
        }else if( otherPhysic instanceof CatBoxPhysicsComponent ){
            if(mySightCone) return; // katze sieht katzenbox → egal
            if ((d = entityManager.getComponent(myEntity, CatPhysicsComponent.class)) != null)
                 entityManager.removeComponent(myEntity, d);
            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                ((CatPropertyComponent)d).isHidden = true;
                
            Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
            for (Integer entity : lasers){
                LaserPointerComponent laserPointerComponent = entityManager.getComponent(entity, LaserPointerComponent.class);
                laserPointerComponent.isVisible = false;
            }
        }
        
        /////////

    }

    @Override
    public void update(float delta) {}

    @Override
    public void render() {}

    @Override
    public void fireEndCollision(PhysixContact contact) {
//        // TODO Auto-generated method stub
//        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();
//        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
//        PhysixEntity other = contact.getOtherPhysixBody().getOwner();
//        
//        if(other instanceof WoolPhysicsComponent){
//            ((WoolPhysicsComponent) other).isSeen = false;
//            Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PlayerComponent.class);
//            CatPropertyComponent player = entityManager.getComponent(compos.get(0), CatPropertyComponent.class);
//            player.isInfluenced = false;
//            
//        }
//        
//        if(other instanceof ConePhysicsComponent){
//            EnemyComponent.seeCat = false;
//        }
        
    }
}


/*

if(other instanceof CatPhysicsComponent){
            logger.debug("cat collides with dog ... or another cat");

        }else if(other instanceof ConePhysicsComponent){
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
            
            Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PlayerComponent.class);
            CatPropertyComponent player = entityManager.getComponent(compos.get(0), CatPropertyComponent.class);
            player.isInfluenced = true;
            
        }else if(other instanceof JumpablePhysicsComponent || other instanceof GroundPhysicsComponent){
            if(contact.getMyFixture().getUserData() != null
                    && contact.getMyFixture().getUserData().equals("masscenter")){
                isCatInZone = true;
            }
            if(! isCatInZone){ return; } // if cat is not in zone stop it here
            
            // search for all entities with physic components
            Array<Integer> compos = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
            for (Integer p : compos) {
                // check if we got the one, we collided with
                PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                if(puddlecompo == other){
                    Component property;
                    property = entityManager.getComponent(p, JumpablePropertyComponent.class);
                    if(property != null){
                        property = entityManager.getComponent(p, GroundPropertyComponent.class);
                    }
                    if( property instanceof JumpablePropertyComponent &&
                            ((JumpablePropertyComponent)property).type == JumpableState.deadzone){
                            // cat fall down
                            Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);
                            if(entities.size > 0)
                            {
                                int player = entities.first();
                                CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);
                                catPropertyComponent.setState(CatStateEnum.FALL);
                            }
                        }
                    else if( property instanceof GroundPropertyComponent){
                        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);
                        if(entities.size > 0)
                        {
                            int player = entities.first();
                            CatPropertyComponent catPropertyComponent = entityManager.getComponent(player, CatPropertyComponent.class);
                            catPropertyComponent.groundWalking = ((GroundPropertyComponent) property).type;
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
        else if(other instanceof StairsPhysicsComponent)
        {
            // TODO: change floor here.
        }

*/