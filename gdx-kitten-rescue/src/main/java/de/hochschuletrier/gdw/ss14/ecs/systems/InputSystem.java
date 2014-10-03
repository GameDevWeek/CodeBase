package de.hochschuletrier.gdw.ss14.ecs.systems;


import de.hochschuletrier.gdw.ss14.ecs.components.*;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.InputState;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;

public class InputSystem extends ECSystem implements GameInputAdapter
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);
    
    public int waterPistol;
    public ParticleEmitterComponent waterParticleEmitter;
    public boolean waterPistolIsOn = false;
    public float timeInFear = 2;

    public InputSystem(EntityManager entityManager)
    {
        super(entityManager, 2);
        InputManager.getInstance().addGameInputAdapter(this);
        
        waterParticleEmitter = new ParticleEmitterComponent();
        waterParticleEmitter.particleTintColor = new Color(0,0,1,0.5f);
        waterParticleEmitter.emitInterval = 0.002f;
        waterParticleEmitter.particleLifetime = 3.0f;
        waterParticleEmitter.emitRadius = 10f;
        
        
        
        waterPistol = entityManager.createEntity();
        entityManager.addComponent(waterPistol, new PhysicsComponent());
    }

    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, CameraComponent.class, PlayerComponent.class);
        Array<Integer> compos2 = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        Array<Integer> compos3 = entityManager.getAllEntitiesWithComponents(EnemyComponent.class);

        for (Integer integer : compos)
        {
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            CameraComponent camComp = entityManager.getComponent(integer, CameraComponent.class);
            LaserPointerComponent laser = entityManager.getComponent(compos2.get(0), LaserPointerComponent.class);
            inputCompo.whereToGo = laser.position;
                        
            Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
            if(laser.input == InputState.MOUSE){
                vec = camComp.smoothCamera.getOrthographicCamera().unproject(vec);
            }
            inputCompo.whereToGo = new Vector2(vec.x, vec.y);
            entityManager.getComponent(waterPistol, PhysicsComponent.class).defaultPosition = inputCompo.whereToGo;
        }
    }

    @Override
    public void render()
    {
    }

    @Override
    public void move(int screenX, int screenY)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveUp(float scale)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveDown(float scale)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveLeft(float scale)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void moveRight(float scale)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void laserButtonPressed()
    {
        //logger.debug("Laser button pressed!");

        Array<Integer> laser = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        if(laser.size > 0)
        {
            for (Integer entity : laser)
            {
                LaserPointerComponent laserPointerComponent = entityManager.getComponent(entity, LaserPointerComponent.class);

                // toggle laser
                if(laserPointerComponent.isVisible == true)
                {
                    laserPointerComponent.isVisible = false;
                }
                else
                {
                    laserPointerComponent.isVisible = true;
                }
            }
        }

        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class);

        for (Integer entity : entities)
        {
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);

            // check if cat should move out of box
            if (catPropertyComponent.isHidden)
            {
                catPropertyComponent.isHidden = !catPropertyComponent.isHidden;

                RenderComponent renderComponent = new RenderComponent();
                entityManager.addComponent(entity, renderComponent);
            }
        }

    }

    @Override
    public void waterPistolButtonDown()
    {
        // TODO Auto-generated method stub
        entityManager.addComponent(waterPistol, waterParticleEmitter);
        waterPistolIsOn = true;
    }

    @Override
    public void waterPistolButtonUp()
    {
        // TODO Auto-generated method stub
        entityManager.removeComponent(waterPistol, waterParticleEmitter);
        waterPistolIsOn = false;
    }

    @Override
    public void menueButtonPressed()
    {
        // TODO Auto-generated method stub

    }
}
