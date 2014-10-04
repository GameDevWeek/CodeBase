package de.hochschuletrier.gdw.ss14.ecs.systems;


import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPropertyComponent;

public class WoolInfluenceSystem extends ECSystem
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);
    
    public WoolInfluenceSystem(EntityManager entityManager)
    {
        super(entityManager, 12);
    }

    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, CameraComponent.class, PlayerComponent.class);
        Array<Integer> compos2 = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        Array<Integer> compos4 = entityManager.getAllEntitiesWithComponents(PhysicsComponent.class, WoolPropertyComponent.class);
        
        WoolPhysicsComponent woool = null;
        CatPropertyComponent catProp;
        CameraComponent camComp;
        LaserPointerComponent laser;
        PhysicsComponent catPhysix = entityManager.getComponent(compos.get(0), PhysicsComponent.class);
        
        for (Integer integer : compos)
        {
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            camComp = entityManager.getComponent(integer, CameraComponent.class);
            laser = entityManager.getComponent(compos2.get(0), LaserPointerComponent.class);
            catProp = entityManager.getComponent(compos.get(0), CatPropertyComponent.class);
            for(Integer entity : compos4){
                PhysicsComponent wool = entityManager.getComponent(entity, PhysicsComponent.class);
                woool = (WoolPhysicsComponent) wool;
                if(woool.isSeen){
                    break;
                }
            }
            if(woool != null){
                if(catProp.isInfluenced){
//                    Vector2 laserPos = laser.position.cpy();
//                    Vector2 laserToWool = woool.getPosition().cpy().sub(laserPos);
//                    catProp.influencedToLaser =  MathUtils.clamp(catProp.influencedToLaser, 0f, 1f);
//                    laserToWool.scl(0.5f);
                    
                    inputCompo.whereToGo.set(woool.getPosition());
                }
                if(catProp.timeTillInfluencedTimer >= catProp.TIME_TILL_INFLUENCED){
                    catProp.timeTillInfluencedTimer = 0;
                }
            }
        }
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }
}
