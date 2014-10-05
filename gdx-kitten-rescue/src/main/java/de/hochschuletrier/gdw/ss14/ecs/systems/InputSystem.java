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
import de.hochschuletrier.gdw.ss14.ecs.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.WoolPhysicsComponent;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class InputSystem extends ECSystem
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);
    
    public float timeInFear = 2;

    public InputSystem(EntityManager entityManager)
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
            
            if(!catProp.isInfluenced){
                catProp.influencedToLaser = 1;
                inputCompo.whereToGo = laser.position;
                            
                Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
                vec = camComp.smoothCamera.getOrthographicCamera().unproject(vec);
                inputCompo.whereToGo = new Vector2(vec.x, vec.y);
           }
            logger.debug("\n"+ catProp.getState());
        }
        
    }

    @Override
    public void render()
    {
    }
}
