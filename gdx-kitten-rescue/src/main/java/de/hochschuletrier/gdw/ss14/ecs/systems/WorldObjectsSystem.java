package de.hochschuletrier.gdw.ss14.ecs.systems;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.JumpablePropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
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

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    
    private ArrayList<CatStateEnum[]> states;
    
    
    public WorldObjectsSystem(EntityManager entityManager) {
        super(entityManager);
        states = new ArrayList<>();
    }

    @Override
    public void stateChanged(CatStateEnum oldstate, CatStateEnum newstate) {
        CatStateEnum [] a = {oldstate, newstate};
        states.add(a);
    }

    private void doing(CatStateEnum oldstate, CatStateEnum newstate) {
        Array<Integer> compos;
        switch(newstate){
        case JUMP:
            compos = entityManager.getAllEntitiesWithComponents(JumpablePropertyComponent.class);
            for (Integer p : compos) {
                PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                puddlecompo.physicsBody.setActive(false);
            }
            break;
        default: break;
        }
        switch(oldstate){
        case JUMP:
            compos = entityManager.getAllEntitiesWithComponents(JumpablePropertyComponent.class);
            for (Integer p : compos) {
                PhysicsComponent puddlecompo = entityManager.getComponent(p, PhysicsComponent.class);
                puddlecompo.physicsBody.setActive(true);
            }
            break;
        default: break;
        }
    }
    
    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        states.forEach((s)->doing(s[0], s[1]));
        states.clear();
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }

}
