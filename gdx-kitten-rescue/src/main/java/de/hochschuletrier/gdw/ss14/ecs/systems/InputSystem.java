package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

public class InputSystem extends ECSystem
{

    public InputSystem(EntityManager entityManager)
    {
        super(entityManager, 1);
    }

    @Override
    public void update(float delta)
    {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, CameraComponent.class, PlayerComponent.class);

        for (Integer integer : compos)
        {
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            CameraComponent camComp = entityManager.getComponent(integer, CameraComponent.class);
            inputCompo.whereToGo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
//          vec = vec.mul(DrawUtil.batch.getProjectionMatrix());
//          inputCompo.whereToGo = new Vector2(vec.x,vec.y);
            vec = camComp.smoothCamera.getOrthographicCamera().unproject(vec);
            inputCompo.whereToGo = new Vector2(vec.x, vec.y);
        }
    }

    @Override
    public void render()
    {
    }
}
