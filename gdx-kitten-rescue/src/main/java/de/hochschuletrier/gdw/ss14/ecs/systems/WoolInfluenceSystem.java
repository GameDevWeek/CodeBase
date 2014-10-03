package de.hochschuletrier.gdw.ss14.ecs.systems;


import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.InputState;
import de.hochschuletrier.gdw.ss14.ecs.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class WoolInfluenceSystem extends ECSystem
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);
    
    public int waterPistol;
    public ParticleEmitterComponent waterParticleEmitter;
    public boolean waterPistolIsOn = false;
    public float timeInFear = 2;

    public WoolInfluenceSystem(EntityManager entityManager)
    {
        super(entityManager, 2);
    }

    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, CameraComponent.class, PlayerComponent.class);
        Array<Integer> compos2 = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        Array<Integer> compos3 = entityManager.getAllEntitiesWithComponents(EnemyComponent.class);
        Array<Integer> compos4 = entityManager.getAllEntitiesWithComponents(WoolPhysicsComponent.class);
        
        for (Integer integer : compos)
        {
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            CameraComponent camComp = entityManager.getComponent(integer, CameraComponent.class);
            LaserPointerComponent laser = entityManager.getComponent(compos2.get(0), LaserPointerComponent.class);
            CatPropertyComponent catProp = entityManager.getComponent(compos.get(0), CatPropertyComponent.class);
            for(Integer entity : compos4){
                WoolPhysicsComponent wool = entityManager.getComponent(entity, WoolPhysicsComponent.class);
                
                if(wool.isSeen){
                    if(catProp.isInfluenced){
                        inputCompo.whereToGo = laser.position;
                        Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
                        if(laser.input == InputState.MOUSE){
                            vec = camComp.smoothCamera.getOrthographicCamera().unproject(vec);
                        }
                        inputCompo.whereToGo = new Vector2(vec.x, vec.y);
                        Vector2 laserToWool = wool.physicsBody.getPosition().sub(laser.position);
                        catProp.influencedToLaser -= delta/catProp.TIME_TILL_INFLUENCED;
                        catProp.timeTillInfluencedTimer += delta;
                        inputCompo.whereToGo = new Vector2(inputCompo.whereToGo.x + laserToWool.x * catProp.influencedToLaser, inputCompo.whereToGo.y + laserToWool.y * catProp.influencedToLaser);
                    }
                    int j = 0;
                }
            }
        }
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }
}
