package de.hochschuletrier.gdw.ss14.ecs.ai;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.*;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.EnemyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.InputComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.systems.BehaviourSystem.GlobalBlackboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class DogBehaviour extends Behaviour {
    private static final Logger logger = LoggerFactory
            .getLogger(DogBehaviour.class);
    
    int dogID;
    public DogBehaviour(int dogID) {
        this.dogID= dogID;
        setName("Catch the Cat");
        BaseNode root = new Sequence(this);        
        new ChaseCat(root);
        
        //BITTE STEHEN LASSEN, VORLAGE:
    //    UntilFailDecorator untFail = new UntilFailDecorator()
        /*  Writer w = new Writer(root, "D");
        new Writer(root, "E");
        new Writer(root, "M");
        new Writer(root, "O");
        RandomChoice random = new RandomChoice(root);
        new Writer(random, "!");
        new Writer(random, "?"); */
        
        setLooping(true);
        //Damit zwischen jagen und patroullieren gewechselt werden muss, muss es ein Loop sein.
    }
    
    class KIAus extends BaseCondition{

        public KIAus(BaseNode parent, Behaviour behaviour) {
            super(parent, behaviour);
            
        }

        @Override
        public State onEvaluate(float delta) {
            
            State result = State.SUCCESS;
            return result;
        }
        
        
    }
    
    class ChaseCat extends BaseTask{

        public ChaseCat(BaseNode parent) {
            super(parent);
            // TODO Auto-generated constructor stub
        }

        @Override
        public State onRun(float delta) {
            GlobalBlackboard bb = (GlobalBlackboard) getGlobalBlackboard();
            Array<Integer> cat= bb.em.getAllEntitiesWithComponents(PlayerComponent.class, PhysicsComponent.class);
            PhysicsComponent cpc =  bb.em.getComponent(cat.first(), PhysicsComponent.class);
            Vector2 pos = cpc.getPosition();
            
            InputComponent dog = bb.em.getComponent(dogID, InputComponent.class);
            dog.whereToGo.set(pos);
            logger.debug("Dog is Chasing!");
            return State.SUCCESS;
        }

        @Override
        public void onActivate() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDeactivate() {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    //Das folgende komplexe Verhalten folgt ggf später, drin lassen:
    /*
    
    class KatzeSichtbar extends BaseCondition{
        //ist die Katze sichtbar?
        public KatzeSichtbar(BaseNode parent, Behaviour behaviour)
        {
            super(parent, behaviour);
        }
     
        public State onEvaluate(float delta)
        {
               //----
               //TODO sobald möglich
               //SUCCESS zurückgeben wenn Katze sichtbar, sonst FAILURE.
               State result = State.SUCCESS;
               return result;
        }
        
        
    }
    
    class BBLeer extends BaseCondition{
     //ist das BlackBoard leer?
        public BBLeer(BaseNode parent, Behaviour behaviour)
        {
            super(parent, behaviour);
        }
        
        public State onEvaluate(float delta)
        {
           
               Blackboard b = ((Blackboard) behaviour.getLocalBlackboard());
               //----
               //TODO wenn Blackboard leer -> success sonst failure.
               //___________
               State result = State.SUCCESS;
               return result;
        }
        
    }
    
    class BBschreiben extends BaseTask{

        public BBschreiben(BaseNode parent) {
            super(parent);
           
        }

        @Override
        public State onRun(float delta) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void onActivate() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void onDeactivate() {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    
    
    
        */

    

    }

    
    
    
