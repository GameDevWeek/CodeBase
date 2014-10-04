package de.hochschuletrier.gdw.ss14.ecs.systems;

import java.util.ArrayList;
import java.util.Vector;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.ToolState;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.physics.ICollisionListener;
import de.hochschuletrier.gdw.ss14.physics.RayCastPhysics;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CatContactSystem extends ECSystem implements ICollisionListener{

    private static final Logger logger = LoggerFactory.getLogger(CatContactSystem.class);

    private PhysixManager phyManager;
    private RayCastPhysics rcp;

//    private ArrayList<Vector2> raycst_start;
//    private ArrayList<Vector2> raycst_end;
    private ArrayList<PhysicsComponent> raycst_startPhys;
    private ArrayList<PhysicsComponent> raycst_targetPhys;
    
    
    public CatContactSystem(EntityManager entityManager, PhysixManager physicsManager){
        super(entityManager);
        phyManager = physicsManager;
//        raycst_start = new ArrayList<>();
//        raycst_end = new ArrayList<>();
        raycst_startPhys = new ArrayList<>();
        raycst_targetPhys = new ArrayList<>();
    }


    private void addRayCast(PhysicsComponent a, PhysicsComponent b){
        raycst_startPhys.add(a);
        raycst_startPhys.add(b);
    }
    
    @Override
    public void fireBeginnCollision(PhysixContact contact){
        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();

        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
        PhysixEntity other = contact.getOtherPhysixBody().getOwner();

        /////////////
        // get all neccessary information
        Array<Integer> physicEntities = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
        Integer myEntity = null, otherEntity = null;
        PhysicsComponent myPhysic = null, otherPhysic = null;
        for(Integer i : physicEntities){
            PhysicsComponent tmp = entityManager.getComponent(i, PhysicsComponent.class);
            if(tmp.physicsBody == contact.getMyPhysixBody()){
                myPhysic = tmp;
                myEntity = i;
            }
            if(tmp.physicsBody == contact.getOtherPhysixBody()){
                otherEntity = i;
                otherPhysic = tmp;
            }
        }
        // checks if the center of the cat is collided, or just a sightcone collision
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
        if(myEntity == null || myPhysic == null || otherEntity == null || otherPhysic == null){
            return;
        }

        Component c = null, d = null;
        /* c → used to check if the other has component xy 
         * d → used to get a specific "my" component to react to the collision
         * */
        if((c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null){
            /*other → is enemy */
            if(otherPhysic instanceof CatPhysicsComponent){
                if(mySightCone){
                    return; // katze sieht hund/ oder sichtfelder berühren sich → egal
                }
                // kollidiert mit hund (oder anderer katze)
                if(otherSightCone){
                    // katzenkörper berührt hunde sichtfeld
                    // melde raycast von hund nach katze an:
                    this.addRayCast(otherPhysic, myPhysic);
                }else{
                    // katzenkörper berührt hundekörper

                }
            }

        }else if((c = entityManager.getComponent(otherEntity, JumpablePropertyComponent.class)) != null){
            /*other → is jumpable object */
            switch(((JumpablePropertyComponent) c).type){
            case deadzone:
                if(!isCatInZone){
                    break;
                }
                if((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null){
                    ((CatPropertyComponent) d).setState(CatStateEnum.FALL);
                }
                break;
            default:
                break;
            }
        }else if((c = entityManager.getComponent(otherEntity, StairsPhysicsComponent.class)) != null){
            if(isCatInZone){
                // katze hat treppe betreten

            }
        }else if((c = entityManager.getComponent(otherEntity, WoolPhysicsComponent.class)) != null){
            /* other → is groundobject */
            if(mySightCone){
                if((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null){
                    ((CatPropertyComponent) d).isInfluenced = true;
                }
            }else{
                //                if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                //                    ((CatPropertyComponent)d)  play with wool
            }
        }else if((c = entityManager.getComponent(otherEntity, GroundPropertyComponent.class)) != null){
            /* other → is groundobject */
            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                ((CatPropertyComponent)d).groundWalking = ((GroundPropertyComponent)c).type;
        }
        else if( otherPhysic instanceof CatBoxPhysicsComponent )
        {
            if(mySightCone) return; // katze sieht katzenbox → egal

            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
            {
                CatPropertyComponent catPropertyComponent = (CatPropertyComponent) d;

                if(!catPropertyComponent.isCatBoxOnCooldown)
                {
                    if ((d = entityManager.getComponent(myEntity, RenderComponent.class)) != null)
                    {
                        entityManager.removeComponent(myEntity, d);
                    }

                    catPropertyComponent.isHidden = true;

                    catPropertyComponent.isCatBoxOnCooldown = true;
                    catPropertyComponent.catBoxCooldownTimer = CatPropertyComponent.CATBOX_COOLDOWN;

                    // toggle laser
                    Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
                    for (Integer entity : lasers)
                    {
                        LaserPointerComponent laserPointerComponent = entityManager.getComponent(entity, LaserPointerComponent.class);
                        laserPointerComponent.toolState = ToolState.WATERPISTOL;
                    }
                }

            }

        }
        else if(other instanceof StairsPhysicsComponent)
        {
            if(mySightCone)
            {
                return;
            }

            int entity = ((StairsPhysicsComponent) other).owner;

            if(entity >= 0){
                StairComponent stairComponent = entityManager.getComponent(entity, StairComponent.class);

                if(stairComponent != null){
                    // TODO: change floor here
                    Game.mapManager.setFloor(stairComponent.targetFloor);

                }

            }

        } // end check for StairPhysicsComponent
        else if(other instanceof FinishPhysicsComponent){
            if(mySightCone){
                return;
            }

            // TODO: goal reached! set outro sequence here.
        }

    }

    @Override
    public void update(float delta){
        /// do all raycast fun:
        for(int i = 0; i < raycst_startPhys.size(); i++){
            phyManager.getWorld().rayCast(rcp, 
                    raycst_startPhys.get(i).physicsBody.getPosition(), 
                    raycst_targetPhys.get(i).physicsBody.getPosition());
            
            for (RayCastPhysics rcst : rcp.collisions) {
                if(rcst.m_fixture.getBody().getUserData() != null &&
                        rcst.m_fixture.getBody().getUserData().equals("wall")){
                    // there is a wall object, we cant see through
                    break;
                }
                
                for(Fixture s : raycst_startPhys.get(i).physicsBody.getFixtureList()){
                    if(s != rcst.m_fixture) continue;
                    
                }
            }
            rcp.reset();
        }
        
    }

    @Override
    public void render(){}

    @Override
    public void fireEndCollision(PhysixContact contact){
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
            if(tmp.physicsBody == contact.getMyPhysixBody()){
                myEntity = i;
            }
            if(tmp.physicsBody == contact.getOtherPhysixBody()){
                otherEntity = i;
                otherPhysic = tmp;
            }
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
        if(myEntity == null || otherEntity == null || otherPhysic == null){
            return;
        }

        Component c = null, d = null;
        if((c = entityManager.getComponent(otherEntity, WoolPhysicsComponent.class)) != null){
            /* other → is groundobject */
            if((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null){
                ((CatPropertyComponent) d).isInfluenced = false;
            }
        }else if((c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null){
            // cat does not collide with dogPhysx anymore which means ...
            if(otherSightCone && !mySightCone){
                // ... dog does not see the cat anymore

            }

        }

    }
}
