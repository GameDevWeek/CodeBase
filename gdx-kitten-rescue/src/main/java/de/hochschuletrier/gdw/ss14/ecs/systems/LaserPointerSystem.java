package de.hochschuletrier.gdw.ss14.ecs.systems;

import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.ToolState;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;

public class LaserPointerSystem extends ECSystem implements GameInputAdapter
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);

    public int waterPistol;
    public ParticleEmitterComponent waterParticleEmitter;
    
    public LaserPointerSystem(EntityManager entityManager)
    {
        super(entityManager, 1);
        InputManager.getInstance().addGameInputAdapter(this);
        
        waterParticleEmitter = new ParticleEmitterComponent();
        waterParticleEmitter.particleTintColor = new Color(0.25f,0.25f,0.5f,0.4f);
        waterParticleEmitter.emitInterval = 0.001f;
        waterParticleEmitter.particleLifetime = 3.0f;
        waterParticleEmitter.emitRadius = 17.5f;
        
        waterPistol = entityManager.createEntity();
        entityManager.addComponent(waterPistol, new PhysicsComponent());
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(lasers.first(), LaserPointerComponent.class);
        
        Array<Integer> cams = entityManager.getAllEntitiesWithComponents(CameraComponent.class);
        CameraComponent cam = entityManager.getComponent(cams.first(), CameraComponent.class);
        
        Vector3 vec = new Vector3(laser.position, 1.0f);
        vec = cam.smoothCamera.getOrthographicCamera().unproject(vec);
        
        Vector2 pos = new Vector2(vec.x, vec.y);
        entityManager.getComponent(waterPistol, PhysicsComponent.class).defaultPosition = pos;
        
        if (laser.waterpistolIsUsed) {
            if (laser.currentWaterlevel > 0) {
                laser.currentWaterlevel = Math.max(laser.currentWaterlevel - (LaserPointerComponent.WATER_CONSUMPTION_SPEED * delta), 0.0f);
                if (laser.currentWaterlevel == 0.0f) {
                    laser.waterpistolIsUsed = false;
                    entityManager.removeComponent(waterPistol, waterParticleEmitter);
                }
            }
        } else {
            if (laser.currentWaterlevel < 1.0f) {
                laser.currentWaterlevel = Math.min(laser.currentWaterlevel + (LaserPointerComponent.WATER_REFILL_SPEED * delta), 1.0f);
            }
        }
    }

    @Override
    public void render()
    {
        //LASERPOINTER RENDERN
    }

    @Override
    public void move(int screenX, int screenY)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        laser.position = new Vector2(screenX, screenY);
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
        Array<Integer> cats = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class);
        CatPropertyComponent cat = entityManager.getComponent(cats.first(), CatPropertyComponent.class);
        
        Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(lasers.first(), LaserPointerComponent.class);
        
        if (cat.isHidden) {
            for (Integer entity : cats)
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
        } else {
            if (!laser.waterpistolIsUsed) {
                if (laser.toolState == ToolState.LASER) {
                    laser.toolState = ToolState.WATERPISTOL;
                    Gdx.input.setCursorImage(laser.waterpistolCursor, 
                            laser.waterpistolCursor.getWidth() / 2, 
                            laser.waterpistolCursor.getHeight() / 2);
                } else {
                    laser.toolState = ToolState.LASER;
                    Gdx.input.setCursorImage(laser.laserCursor, 
                            laser.laserCursor.getWidth() / 2, 
                            laser.laserCursor.getHeight() / 2);
                }
                
                for (Integer entity : cats)
                {
                    CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);

                    if (catPropertyComponent.canSeeLaserPointer == true)
                    {
                        catPropertyComponent.canSeeLaserPointer = false;
                        SoundManager.performAction(LaserPointerActions.OFF);
                    }
                    else
                    {
                        catPropertyComponent.canSeeLaserPointer = true;
                        SoundManager.performAction(LaserPointerActions.ON);
                    }
                }
            }
        }
    }

    @Override
    public void waterPistolButtonDown()
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        if (laser.toolState == ToolState.WATERPISTOL && laser.currentWaterlevel > 0) {
            laser.waterpistolIsUsed = true;
            entityManager.addComponent(waterPistol, waterParticleEmitter);
        }
    }

    @Override
    public void waterPistolButtonUp()
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        if (laser.toolState == ToolState.WATERPISTOL && laser.waterpistolIsUsed) {
            laser.waterpistolIsUsed = false;
            entityManager.removeComponent(waterPistol, waterParticleEmitter);
        }
    }

    @Override
    public void menueButtonPressed()
    {
        // TODO Auto-generated method stub

    }
}
