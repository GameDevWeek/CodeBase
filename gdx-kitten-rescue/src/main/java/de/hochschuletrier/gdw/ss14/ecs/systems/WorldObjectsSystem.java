package de.hochschuletrier.gdw.ss14.ecs.systems;

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
    
    private CatStateEnum oldstate, newstate;
    private boolean dirty;
    
    public WorldObjectsSystem(EntityManager entityManager) {
        super(entityManager);
        dirty = false;
    }

    @Override
    public void stateChanged(CatStateEnum oldstate, CatStateEnum newstate) {
        this.dirty = true;
        this.oldstate = oldstate;
        this.newstate = newstate;

    }

    @Override
    public void update(float delta) {
        // TODO Auto-generated method stub
        if(! dirty) return;
        if(this.newstate == null) return; 
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
        this.oldstate = null;
        this.newstate = null;
        this.dirty = false;
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }

}
