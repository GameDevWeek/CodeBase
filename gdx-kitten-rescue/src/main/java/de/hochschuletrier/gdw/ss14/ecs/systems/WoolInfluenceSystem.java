package de.hochschuletrier.gdw.ss14.ecs.systems;


import org.slf4j.LoggerFactory;

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
                    logger.debug("\nIST BEEINFLUSST");
                    inputCompo.whereToGo = laser.position;
                    Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
                    vec = camComp.smoothCamera.getOrthographicCamera().unproject(vec);
                    inputCompo.whereToGo = new Vector2(vec.x, vec.y);
                    Vector2 laserToWool = woool.physicsBody.getPosition().sub(inputCompo.whereToGo);
                    catProp.influencedToLaser -= delta/catProp.TIME_TILL_INFLUENCED;
                    catProp.timeTillInfluencedTimer += delta;
                    logger.debug("\nLaserPosition: (" + inputCompo.whereToGo.x + ", " + inputCompo.whereToGo.y + 
                                ")\nWoolPosition: (" + woool.physicsBody.getX() + ", " + woool.physicsBody.getY() + 
                                ")\nLaserToWool: (" + laserToWool.x + ", " + laserToWool.y + ")" +
                                "\nDistance: " + laserToWool.len());
                    inputCompo.whereToGo = new Vector2(inputCompo.whereToGo.x + laserToWool.x * catProp.influencedToLaser, inputCompo.whereToGo.y + laserToWool.y * catProp.influencedToLaser);
                    logger.debug("\nNEW POSITION: (" + inputCompo.whereToGo.x + ", " + inputCompo.whereToGo.y + ")");
                    System.out.println("");
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
