package de.hochschuletrier.gdw.ss14.ecs.systems;

import de.hochschuletrier.gdw.ss14.ecs.components.*;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.ToolState;
import de.hochschuletrier.gdw.ss14.input.GameInputAdapter;
import de.hochschuletrier.gdw.ss14.input.InputManager;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;

public class LaserPointerSystem extends ECSystem implements GameInputAdapter
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);
    
    private static final float laserPointerAlpha = 0.75f;
    private float lastMousePostionX = 0;
    private float lastMousePositionY = 0;

    public int waterPistol;
    public ParticleEmitterComponent waterParticleEmitter;
    
    public Texture laserCursor;
    public Texture waterpistolCursor;
    
    public LaserPointerSystem(EntityManager entityManager)
    {
        super(entityManager, 2048);
        InputManager.getInstance().addGameInputAdapter(this);
        
        waterParticleEmitter = new ParticleEmitterComponent();
        waterParticleEmitter.particleTintColor = new Color(0.25f,0.25f,0.5f,0.4f);
        waterParticleEmitter.emitInterval = 0.001f;
        waterParticleEmitter.particleLifetime = 3.0f;
        waterParticleEmitter.emitRadius = 17.5f;
        
        waterPistol = entityManager.createEntity();
        entityManager.addComponent(waterPistol, new PhysicsComponent());
        entityManager.addComponent(waterPistol, new WaterPistolComponent());
        
        laserCursor = EntityFactory.assetManager.getTexture("laser_cursor");
        waterpistolCursor = EntityFactory.assetManager.getTexture("waterpistol_cursor");
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

        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(WaterPistolComponent.class);
        if(entities.size>0)
        {
            waterPistol = entities.first();
        }

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
        Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(lasers.first(), LaserPointerComponent.class);
        
        Array<Integer> cams = entityManager.getAllEntitiesWithComponents(CameraComponent.class);
        CameraComponent cam = entityManager.getComponent(cams.first(), CameraComponent.class);
        
        Vector3 vec = new Vector3(laser.position, 1.0f);
        vec = cam.smoothCamera.getOrthographicCamera().unproject(vec);
        
        Texture cursor = null;


        // don't draw cursor if laser pointer off.
        if(laser.toolState == ToolState.WATERPISTOL)
        {
            return;
        }

        if (laser.toolState == ToolState.LASER) {
            cursor = laserCursor;
        } else {
            cursor = waterpistolCursor;
        }
        
        DrawUtil.batch.setColor(1, 1, 1, laserPointerAlpha);
        DrawUtil.batch.draw(cursor, vec.x - (cursor.getWidth() / 2), vec.y - (cursor.getHeight() / 2));
        DrawUtil.batch.setColor(1,1,1,1);
    }

    @Override
    public void move(int screenX, int screenY)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        
        // calculate delta because the screenX/screenY vars can be outside of the game
        float deltaX, deltaY;
        float newPosX, newPosY;
        deltaX = screenX - lastMousePostionX;
        deltaY = screenY - lastMousePositionY;
        newPosX = laser.position.x + deltaX;
        newPosY = laser.position.y + deltaY;
        lastMousePostionX = screenX;
        lastMousePositionY = screenY;
        
        //limit position to screen
        if (newPosX > Gdx.graphics.getWidth()) {
            newPosX = Gdx.graphics.getWidth();
        }
        if (newPosX < 0) {
            newPosX = 0;
        }
        if (newPosY > Gdx.graphics.getHeight()) {
            newPosY = Gdx.graphics.getHeight();
        }
        if (newPosY < 0) {
            newPosY = 0;
        }
        
        laser.position = new Vector2(newPosX, newPosY);
    }

    @Override
    public void moveUp(float scale)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        
        float newY = laser.position.y - (scale * 10.0f);
        
        //limit position to screen
        if (newY > Gdx.graphics.getHeight()) {
            newY = Gdx.graphics.getHeight();
        }
        if (newY < 0) {
            newY = 0;
        }
        
        laser.position = new Vector2(laser.position.x, newY);
    }

    @Override
    public void moveDown(float scale)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        
        float newY = laser.position.y + (scale * 10.0f);
        
        //limit position to screen
        if (newY > Gdx.graphics.getHeight()) {
            newY = Gdx.graphics.getHeight();
        }
        if (newY < 0) {
            newY = 0;
        }
        
        laser.position = new Vector2(laser.position.x, newY);
    }

    @Override
    public void moveLeft(float scale)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        
        float newX = laser.position.x - (scale * 10.0f);
        
        //limit position to screen
        if (newX > Gdx.graphics.getWidth()) {
            newX = Gdx.graphics.getWidth();
        }
        if (newX < 0) {
            newX = 0;
        }
        
        laser.position = new Vector2(newX - (scale * 10.0f), laser.position.y);
    }

    @Override
    public void moveRight(float scale)
    {
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        
        float newX = laser.position.x + (scale * 10.0f);
        
        //limit position to screen
        if (newX > Gdx.graphics.getWidth()) {
            newX = Gdx.graphics.getWidth();
        }
        if (newX < 0) {
            newX = 0;
        }
        
        laser.position = new Vector2(newX, laser.position.y);
    }

    @Override
    public void laserButtonPressed()
    {
        Array<Integer> cats = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class);
        CatPropertyComponent cat = entityManager.getComponent(cats.first(), CatPropertyComponent.class);

        Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(lasers.first(), LaserPointerComponent.class);

        if (cat.isHidden)
        {
            for (Integer entity : cats)
            {
                CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);

                // check if cat should move out of box
                if (catPropertyComponent.isHidden)
                {
                    catPropertyComponent.isHidden = !catPropertyComponent.isHidden;

                    RenderComponent renderComponent = new RenderComponent();
                    entityManager.addComponent(entity, renderComponent);

                    laser.toolState = ToolState.LASER;

                    if(laser.toolState == ToolState.LASER)
                    {
                        SoundManager.performAction(LaserPointerActions.ON);
                        if(catPropertyComponent != null)
                        {
                            catPropertyComponent.canSeeLaserPointer = true;
                        }
                    }
                    else if(laser.toolState == ToolState.WATERPISTOL)
                    {
                        //SoundManager.performAction(LaserPointerActions.OFF);
                        if(catPropertyComponent != null)
                        {
                            catPropertyComponent.canSeeLaserPointer = false;
                        }
                    }
                }
            }
        }

        if (laser.toolState == ToolState.LASER)
        {
            laser.toolState = ToolState.WATERPISTOL;
        } else {
            laser.toolState = ToolState.LASER;
        }

//        CatPropertyComponent catPropertyComponent = null;
//        for (Integer entity : cats)
//        {
//            catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
//        }

        /*
        Array<Integer> cats = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class);
        CatPropertyComponent cat = entityManager.getComponent(cats.first(), CatPropertyComponent.class);
        
        Array<Integer> lasers = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(lasers.first(), LaserPointerComponent.class);
        
            if (!laser.waterpistolIsUsed)
            {
                if (cat.isHidden)
                {
                    for (Integer entity : cats)
                    {
                        CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);

                        // check if cat should move out of box
                        if (catPropertyComponent.isHidden)
                        {
                            catPropertyComponent.isHidden = !catPropertyComponent.isHidden;

                            RenderComponent renderComponent = new RenderComponent();
//                            entityManager.addComponent(entity, renderComponent);
                        }
                    }
                }

                if (laser.toolState == ToolState.LASER) {
                    laser.toolState = ToolState.WATERPISTOL;
                } else {
                    laser.toolState = ToolState.LASER;
                }

                CatPropertyComponent catPropertyComponent = null;
                for (Integer entity : cats)
                {
                    catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
                }

                if(laser.toolState == ToolState.LASER)
                {
                    SoundManager.performAction(LaserPointerActions.ON);
                    if(catPropertyComponent != null)
                    {
                        catPropertyComponent.canSeeLaserPointer = true;
                    }
                }
                else if(laser.toolState == ToolState.WATERPISTOL)
                {
                    SoundManager.performAction(LaserPointerActions.OFF);
                    if(catPropertyComponent != null)
                    {
                        catPropertyComponent.canSeeLaserPointer = false;
                    }
                }
            }
            */
    }

    @Override
    public void waterPistolButtonDown()
    {
        /*
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        if (laser.toolState == ToolState.WATERPISTOL && laser.currentWaterlevel > 0) {
            laser.waterpistolIsUsed = true;
            entityManager.addComponent(waterPistol, waterParticleEmitter);
        }
        */
    }

    @Override
    public void waterPistolButtonUp()
    {
        /*
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);
        LaserPointerComponent laser = entityManager.getComponent(compos.first(), LaserPointerComponent.class);
        if (laser.toolState == ToolState.WATERPISTOL && laser.waterpistolIsUsed) {
            laser.waterpistolIsUsed = false;
            entityManager.removeComponent(waterPistol, waterParticleEmitter);
        }
        */
    }

    @Override
    public void menueButtonPressed()
    {
        // TODO Auto-generated method stub

    }
}
