package de.hochschuletrier.gdw.ss14.ecs.systems;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;

import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.ai.DogBehaviour;
import de.hochschuletrier.gdw.ss14.ecs.components.BehaviourComponent;

public class BehaviourSystem extends ECSystem{

    public BehaviourManager behaviourManager;
    public GlobalBlackboard globalBlackboard;
    public BehaviourSystem(EntityManager entityManager, BehaviourManager behaviourManager) {
        super(entityManager);        
        this.behaviourManager = behaviourManager;
    }

    @Override
    public void update(float delta) {
        behaviourManager.update(delta);
        //System.out.println("System updating");
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }
    
    public static class GlobalBlackboard{
        public EntityManager em;
        
        public GlobalBlackboard(EntityManager enm)
        { //Konstruktor ohne Argumente würde keinen Sinn machen, daher weggelassen.
            em = enm;
        }
        
      //  Array<Integer> dogEntities = em.getAllEntitiesWithComponents(InputComponent.class, EnemyComponent.class);
    }

}
