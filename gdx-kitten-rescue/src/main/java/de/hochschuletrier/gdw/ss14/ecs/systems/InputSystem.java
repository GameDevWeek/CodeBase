package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.components.LaserPointerComponent.*;
import de.hochschuletrier.gdw.ss14.input.*;
import org.slf4j.*;

public class InputSystem extends ECSystem implements GameInputAdapter
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(InputSystem.class);

    public InputSystem(EntityManager entityManager)
    {
        super(entityManager, 2);
        InputManager.getInstance().addGameInputAdapter(this);
    }

    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, CameraComponent.class, PlayerComponent.class);
        Array<Integer> compos2 = entityManager.getAllEntitiesWithComponents(LaserPointerComponent.class);

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

        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class);

        for (Integer entity : entities)
        {
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);


            // toggle laser
            if(catPropertyComponent.canSeeLaserPointer == true)
            {
                catPropertyComponent.canSeeLaserPointer = false;
            }
            else
            {
                catPropertyComponent.canSeeLaserPointer = true;
            }

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

    }

    @Override
    public void waterPistolButtonUp()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void menueButtonPressed()
    {
        // TODO Auto-generated method stub

    }
}
