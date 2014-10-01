package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.*;

public class DogInputSystem extends ECSystem{

    public DogInputSystem(EntityManager entityManager){
        super(entityManager, 1);
    }

    @Override
    public void update(float delta){
        Array<Integer> compos = entityManager.getAllEntitiesWithComponents(InputComponent.class, EnemyComponent.class);
        Array<Integer> compos2 = entityManager.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);
        for(Integer integer : compos){
            InputComponent inputCompo = entityManager.getComponent(integer, InputComponent.class);
            PhysicsComponent phyCompo = entityManager.getComponent(compos2.get(0), PhysicsComponent.class);
            inputCompo.whereToGo = phyCompo.getPosition();
            CameraComponent camComp = entityManager.getComponent(integer, CameraComponent.class);
            Vector3 vec = new Vector3(inputCompo.whereToGo.x, inputCompo.whereToGo.y, 1);
            vec = camComp.smoothCamera.getOrthographicCamera().unproject(vec);
            inputCompo.whereToGo = new Vector2(vec.x, vec.y);
        }
    }

    @Override
    public void render(){
    }
}
