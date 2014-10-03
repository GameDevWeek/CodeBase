package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.physics.ICatStateListener;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;


/**
 * This System changes WorldObject Collisions based on the cat state.
 * 
 * It allows to jump over puddles for example.
 * 
 * @author oliver
 *
 */
public class WorldObjectsSystem extends ECSystem implements ICatStateListener {

    public WorldObjectsSystem(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void stateChanged(CatStateEnum oldstate, CatStateEnum newstate) {
        Array<Integer> compos;
        switch(newstate){
        case JUMP_BEGIN:
            compos = entityManager.getAllEntitiesWithComponents(JumpablePropertyComponent.class);
            for (Integer p : compos) {
                PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                puddlecompo.physicsBody.setActive(false);
            }
            break;
        case JUMP_END:
            compos = entityManager.getAllEntitiesWithComponents(JumpablePropertyComponent.class);
            for (Integer p : compos) {
                PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                puddlecompo.physicsBody.setActive(true);
            }
            break;
        default:
            break;
        
        }
        
    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }

}
