package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixEntity;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import static de.hochschuletrier.gdw.ss14.ecs.EntityFactory.assetManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatBoxPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.Component;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.FinishPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.GroundPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.ToolState;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.StairComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.StairsPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.physics.ICollisionListener;
import de.hochschuletrier.gdw.ss14.physics.RayCastPhysics;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

public class CatContactSystem extends ECSystem implements ICollisionListener
{

    private static final Logger logger = LoggerFactory.getLogger(CatContactSystem.class);

    private PhysixManager phyManager;
    private RayCastPhysics rcp = new RayCastPhysics();

    //    private ArrayList<Vector2> raycst_start;
//    private ArrayList<Vector2> raycst_end;
    private ArrayList<PhysicsComponent> raycst_startPhys;
    private ArrayList<PhysicsComponent> raycst_targetPhys;


    public CatContactSystem(EntityManager entityManager, PhysixManager physicsManager)
    {
        super(entityManager);
        phyManager = physicsManager;
//        raycst_start = new ArrayList<>();
//        raycst_end = new ArrayList<>();
        raycst_startPhys = new ArrayList<>();
        raycst_targetPhys = new ArrayList<>();
    }


    private void addRayCast(PhysicsComponent a, PhysicsComponent b)
    {

        // Don't add if the pair already exists.
        if ((raycst_startPhys.contains(a)) && (raycst_targetPhys.contains(b)))
        {

            if ((raycst_startPhys.indexOf(a) == raycst_targetPhys.indexOf(b)))
                return;
        }

        raycst_startPhys.add(a);
        raycst_targetPhys.add(b);
    }

    @Override
    public void fireBeginnCollision(PhysixContact contact)
    {
        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();

        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
        PhysixEntity other = contact.getOtherPhysixBody().getOwner();

        /////////////
        // get all neccessary information
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
        // checks if the center of the cat is collided, or just a sightcone collision
        boolean isCatInZone = false, mySightCone = false, otherSightCone = false;
        if (contact.getMyFixture().getUserData() != null)
        {
            if (contact.getMyFixture().getUserData().equals("masscenter"))
            {
                isCatInZone = true;
            }
            else if (contact.getMyFixture().getUserData().equals("sightcone"))
            {
                mySightCone = true;
            }
        }
        if (contact.getOtherFixture().getUserData() != null)
        {
            if (contact.getOtherFixture().getUserData().equals("sightcone"))
            {
                otherSightCone = true;
            }
        }

        //////////
        // if something wierd happens, one of these is null then dont go on
        if (myEntity == null || myPhysic == null || otherEntity == null || otherPhysic == null)
        {
            return;
        }

        Component c = null, d = null;
        /* c → used to check if the other has component xy
         * d → used to get a specific "my" component to react to the collision
         * */
        if ((c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null)
        {
            /*other → is enemy */
            if (otherPhysic instanceof CatPhysicsComponent)
            {
                if (mySightCone)
                {
                    return; // katze sieht hund/ oder sichtfelder berühren sich → egal
                }
                // kollidiert mit hund (oder anderer katze)
                if (otherSightCone)
                {
                    // katzenkörper berührt hunde sichtfeld
                    // melde raycast von hund nach katze an:
                    this.addRayCast(otherPhysic, myPhysic);
                }
                else
                {
                    // katzenkörper berührt hundekörper
                    CatPropertyComponent catPropertyComponent = entityManager.getComponent(myEntity, CatPropertyComponent.class);

                    if(catPropertyComponent != null)
                    {
                        if(catPropertyComponent.getState() != CatStateEnum.DIE && catPropertyComponent.getState() != CatStateEnum.FALL)
                        {
                            catPropertyComponent.setState(CatStateEnum.DIE);
                        }
                    }

                }
            }

        }
        else if ((c = entityManager.getComponent(otherEntity, JumpablePropertyComponent.class)) != null)
        {
            /*other → is jumpable object */
            switch (((JumpablePropertyComponent) c).type)
            {
                case deadzone:
                    if (!isCatInZone)
                    {
                        break;
                    }
                    if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                    {
                        ((CatPropertyComponent) d).setState(CatStateEnum.FALL);
                        SoundManager.performAction(CatStateEnum.FALL); // sound activate falling cat
                    }
                    break;
                case waterpuddle:
                    if (!isCatInZone)
                    {
                        break;
                    }
                    if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                    {
                        ((CatPropertyComponent) d).setState(CatStateEnum.DIE);
                        SoundManager.performAction(CatStateEnum.DIE); // sound activate falling cat
                    }
                    break;
                case bloodpuddle:
                    if (!isCatInZone)
                    {
                        break;
                    }
                    if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                    {
                        ((CatPropertyComponent) d).setState(CatStateEnum.DIE);
                        SoundManager.performAction(CatStateEnum.DIE); // sound activate falling cat
                    }
                    break;
                default:
                    break;
            }
        }
        else if (otherPhysic instanceof WoolPhysicsComponent || (c = entityManager.getComponent(otherEntity, WoolPhysicsComponent.class)) != null)
        {
            /* other → is groundobject */
            d = entityManager.getComponent(myEntity, CatPropertyComponent.class);
            if (mySightCone)
            {
                if (d != null)
                {
                    ((CatPropertyComponent) d).isInfluenced = true;
                    ((WoolPhysicsComponent) otherPhysic).isSeen = true;
                }
            }
            else
            {
                entityManager.deletePhysicEntity(otherPhysic.owner);
                ((CatPropertyComponent) d).setState(CatStateEnum.PLAYS_WITH_WOOL);
            }
        }
        else if ((c = entityManager.getComponent(otherEntity, GroundPropertyComponent.class)) != null)
        {
            /* other → is groundobject */
            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
                ((CatPropertyComponent) d).groundWalking = ((GroundPropertyComponent) c).type;
        }
        else if (otherPhysic instanceof CatBoxPhysicsComponent)
        {
            if (mySightCone) return; // katze sieht katzenbox → egal

            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null)
            {
                CatPropertyComponent catPropertyComponent = (CatPropertyComponent) d;

                if (!catPropertyComponent.isCatBoxOnCooldown)
                {
                    RenderComponent boxRender = entityManager.getComponent(otherEntity, RenderComponent.class);
                    if(boxRender != null) {
                        boxRender.texture = new TextureRegion(assetManager.getTexture("box_with_cat"));
                    }
                    if ((d = entityManager.getComponent(myEntity, RenderComponent.class)) != null)
                    {
//                        entityManager.removeComponent(myEntity, d);
                    }

                    catPropertyComponent.setState(CatStateEnum.JUMPING_IN_BOX);
                    catPropertyComponent.isHidden = true;
                    SoundManager.performAction(CatStateEnum.JUMPING_IN_BOX);

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
        else if (other instanceof StairsPhysicsComponent)
        {
            if (mySightCone)
            {
                return;
            }

            int entity = ((StairsPhysicsComponent) other).owner;

            if (entity >= 0)
            {
                CatPropertyComponent catPropertyComponent = entityManager.getComponent(myEntity, CatPropertyComponent.class);

                if (catPropertyComponent != null && catPropertyComponent.canChangeMap)
                {
                    catPropertyComponent.canChangeMap = false;

                    StairComponent stairComponent = entityManager.getComponent(entity, StairComponent.class);
                    if (stairComponent != null && stairComponent.changeFloorDirection > 0)
                    {
                        // TODO: change floor here
                        catPropertyComponent.idOfLastTouchedStair = ((StairsPhysicsComponent) other).owner;
                        Game.mapManager.targetFloor = Game.mapManager.currentFloor + stairComponent.changeFloorDirection;
                        Game.mapManager.isChangingFloor = true;
                        //Game.mapManager.setFloor(stairComponent.targetFloor);

                    }
                }

            }

        } // end check for StairPhysicsComponent
        else if (other instanceof FinishPhysicsComponent)
        {
            if (mySightCone)
            {
                return;
            }

            // TODO: goal reached! set outro sequence here.
        }


    }

    @Override
    public void fireEndCollision(PhysixContact contact)
    {
        PhysixBody owner = contact.getMyPhysixBody();//.getOwner();

        Object o = contact.getOtherPhysixBody().getFixtureList().get(0).getUserData();
        PhysixEntity other = contact.getOtherPhysixBody().getOwner();

        /////////////
        // get all neccessary information
        Array<Integer> physicEntities = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
        Integer myEntity = null, otherEntity = null;
        PhysicsComponent myPhysic = null, otherPhysic = null;
        for (Integer i : physicEntities)
        {
            PhysicsComponent tmp = entityManager.getComponent(i, PhysicsComponent.class);
            if (tmp.physicsBody == contact.getMyPhysixBody())
            {
                myEntity = i;
                myPhysic = tmp;
            }
            if (tmp.physicsBody == contact.getOtherPhysixBody())
            {
                otherEntity = i;
                otherPhysic = tmp;
            }
        }
        // checks if the center of the cat is collided
        boolean isCatInZone = false, mySightCone = false, otherSightCone = false;
        if (contact.getMyFixture().getUserData() != null)
        {
            if (contact.getMyFixture().getUserData().equals("masscenter"))
            {
                isCatInZone = true;
            }
            else if (contact.getMyFixture().getUserData().equals("sightcone"))
            {
                mySightCone = true;
            }
        }
        if (contact.getOtherFixture().getUserData() != null)
        {
            if (contact.getOtherFixture().getUserData().equals("sightcone"))
            {
                otherSightCone = true;
            }
        }

        //////////
        // if something wierd happens, one of these is null then dont go on
        if (myEntity == null || otherEntity == null || otherPhysic == null)
        {
            return;
        }

        Component c = null, d = null;
        if (otherPhysic instanceof WoolPhysicsComponent || (c = entityManager.getComponent(otherEntity, WoolPhysicsComponent.class)) != null)
        {
            /* other → is groundobject */
            if ((d = entityManager.getComponent(myEntity, CatPropertyComponent.class)) != null){
                ((CatPropertyComponent)d).isInfluenced = false;
                EnemyComponent eec = entityManager.getComponent(otherEntity, EnemyComponent.class);
                if(eec != null)
                  eec.seeCat = false;
            }
                ((WoolPhysicsComponent)otherPhysic).isSeen = false;

        }else if( (c = entityManager.getComponent(otherEntity, RenderComponent.class)) != null ){
            RenderComponent renderCompFromBox = (RenderComponent)c;
            renderCompFromBox.texture = new TextureRegion(assetManager.getTexture("catbox"));
        }else if( (c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null ){
            EnemyComponent enemyComponent = entityManager.getComponent(otherEntity, EnemyComponent.class);
            enemyComponent.seeCat = false;
        }
        else if ((c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null)
        {
            EnemyComponent enemyComponent = entityManager.getComponent(otherEntity, EnemyComponent.class);
            enemyComponent.seeCat = false;
        }
        else
        {
            if ((c = entityManager.getComponent(otherEntity, EnemyComponent.class)) != null)
            {
                // cat does not collide with dogPhysx anymore which means ...
                if (otherSightCone && !mySightCone)
                {
                    // ... dog does not see the cat anymore
                    this.removeRayCast(otherPhysic, myPhysic);
                }
                EnemyComponent enemyComponent = entityManager.getComponent(otherEntity, EnemyComponent.class);
                enemyComponent.seeCat = false;
            }
        }

        if (other instanceof StairsPhysicsComponent)
        {
            if (mySightCone)
            {
                return;
            }

            int entity = ((StairsPhysicsComponent) other).owner;

            CatPropertyComponent catPropertyComponent = entityManager.getComponent(myEntity, CatPropertyComponent.class);
            StairComponent stairComponent = entityManager.getComponent(entity, StairComponent.class);

            if (contact.getMyFixture().getUserData() != null && contact.getMyFixture().getUserData().equals("masscenter"))
            {
                if (catPropertyComponent != null && !catPropertyComponent.canChangeMap)
                {
                    if (catPropertyComponent.idOfLastTouchedStair != ((StairsPhysicsComponent) other).owner)
                    {
                        catPropertyComponent.canChangeMap = true;
                    }
                }
            }


        }

    }

    private void removeRayCast(PhysicsComponent a, PhysicsComponent b)
    {
        int i = 0;
        while (!(raycst_startPhys.get(i) == a && raycst_targetPhys.get(i) == b)
                && !(i > raycst_startPhys.size()))
        {
            i++;
        }
        raycst_startPhys.remove(i);
        raycst_targetPhys.remove(i);
    }

    @Override
    public void render()
    {
    }

    @Override
    public void update(float delta)
    {
        /// do all raycast fun:
        for (int i = 0; i < raycst_startPhys.size(); ++i)
        {
            phyManager.getWorld().rayCast(rcp,
                    raycst_startPhys.get(i).physicsBody.getPosition(),
                    raycst_targetPhys.get(i).physicsBody.getPosition());
            boolean freeSight = false;
            for (RayCastPhysics rcst : rcp.collisions)
            {
                if (rcst.m_fixture.getBody().getUserData() != null &&
                        rcst.m_fixture.getBody().getUserData().equals("wall"))
                {
                    // there is a wall object, we cant see through
                    break;
                }

                for (Fixture s : raycst_startPhys.get(i).physicsBody.getFixtureList())
                {
                    if ((s.getUserData() != null &&
                            s.getUserData().equals("sightcone")) ||  // target has to be sightcone
                            s != rcst.m_fixture) continue;          // target has to be one of the fixtures
                    freeSight = true;
                }
            }
            /////////////
            // get all neccessary information
            Array<Integer> physicEntities = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class);
            Integer startEntity = null, targetEntity = null;
            for (Integer entity : physicEntities)
            {
                PhysicsComponent tmp = entityManager.getComponent(entity, PhysicsComponent.class);
                if (tmp.physicsBody == raycst_startPhys.get(i).physicsBody)
                {
                    startEntity = entity;
                }
                if (tmp.physicsBody == raycst_targetPhys.get(i).physicsBody)
                {
                    targetEntity = entity;
                }
            }

            Component c = null, d = null;
            /* c → used to check if the other has component xy
             * d → used to get a specific "my" component to react to the collision
             * */

            if (startEntity != null && targetEntity != null)
            {
                c = entityManager.getComponent(startEntity, EnemyComponent.class);
                EnemyComponent enemyComponent = entityManager.getComponent(startEntity, EnemyComponent.class);
                if (c != null)
                {
                    d = entityManager.getComponent(targetEntity, PlayerComponent.class);
                    if (d != null)
                    {
                        //dog sees cat
                        enemyComponent.seeCat = true;
                    }

                }
                else
                {
                    c = entityManager.getComponent(startEntity, PlayerComponent.class);
                    if (c != null)
                    {

                        if (raycst_targetPhys.get(i) instanceof WoolPhysicsComponent)
                        {
                            //cat sees wool
                        }
                    }
                }
            }


            rcp.reset();
        }

    }
}
