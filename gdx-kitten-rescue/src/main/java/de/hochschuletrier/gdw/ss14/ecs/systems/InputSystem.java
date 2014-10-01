package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;

public class InputSystem extends ECSystem{

    public InputSystem(EntityManager entityManager) {
        super(entityManager, 1);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, PlayerComponent.class);

        for (Integer integer : compos) {
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            CameraComponent camComp = entityManager.getComponent(integer, CameraComponent.class);
            inputCompo.whereToGo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
//            Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
//            vec.mul(DrawUtil.batch.getProjectionMatrix());
//            inputCompo.whereToGo = new Vector2(vec.x,vec.y);
        }
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub

    }

}
