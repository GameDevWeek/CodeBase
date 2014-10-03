package de.hochschuletrier.gdw.ss14.ecs.ai;


import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.*;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.decorators.Invert;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.DogPhysicsComponent;
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

    float x, y;
    DogBlackboard bb;
    InputComponent ic;
    PhysicsComponent pc;
    Array<Integer> cat;
    PhysicsComponent cpc;
    
    public DogBehaviour(String name, Object localBlackboard, boolean isLooping,
            int dogID) {
        super(name, localBlackboard, isLooping);
        
        this.dogID = dogID;
        setName("Catch the Cat und weich Ecken aus");
        /* Setup Blackboard informations*/
        bb = (DogBlackboard) localBlackboard;
        ic = bb.em
               .getComponent(dogID, InputComponent.class);
        pc = bb.em
               .getComponent(dogID, PhysicsComponent.class);
        //muss schon Katze existieren...
       /* cat = bb.em.getAllEntitiesWithComponents(
                PlayerComponent.class, PhysicsComponent.class);
        cpc = bb.em.getComponent(cat.first(),
                PhysicsComponent.class);
        */
        /* Setup Tree */
      BaseNode root = new Sequence(this);
      new DogBehaviour.HundHaengt(root, this);
        /*
          
          BaseNode root = new Selector(this);
         
        Sequence hundHaengt = new Sequence(root);
        Sequence dogChase = new Sequence(root);
        new DogBehaviour.HundHaengt(hundHaengt, this);
        new DogBehaviour.HundInRandomRichtung(hundHaengt);
        new DogBehaviour.ChaseCat(dogChase);
        Invert merke = new Invert(dogChase, this);
        new DogBehaviour.HundHaengt(merke, this);
      */

        
        
        // BITTE STEHEN LASSEN, VORLAGE:
        // UntilFailDecorator untFail = new UntilFailDecorator()
        /*
         * Writer w = new Writer(root, "D"); new Writer(root, "E"); new
         * Writer(root, "M"); new Writer(root, "O"); RandomChoice random = new
         * RandomChoice(root); new Writer(random, "!"); new Writer(random, "?");
         */

        setLooping(true);
        // Damit zwischen jagen und patroullieren gewechselt werden muss, muss
        // es ein Loop sein.
    }

    
    
    class KIAus extends BaseCondition {

        public KIAus(BaseNode parent, Behaviour behaviour) {
            super(parent, behaviour);

        }

        @Override
        public State onEvaluate(float delta) {

            State result = State.SUCCESS;
            return result;
        }

    }
    
    class HundHaengt extends BaseCondition {
        //Gibt Success zurück, wenn der Hund an einer Stelle hängt für eine gewisse Zeit.
        
        float timer;
        float MAX_TIME = 2;
        Vector2 pos ;
        Vector2 current;
        PhysicsComponent pcNext;
        Vector2 posNext;
        boolean zeitpunkt;
        public HundHaengt(BaseNode parent, Behaviour behaviour) {
            super(parent, behaviour);
            timer = MAX_TIME*2; //*2 wegen differenzierter implementierung. zeitraum wird geteilt.
           pos = new Vector2();
           posNext = new Vector2();
            //Beim Konstruktor hat DogBehaviour noch keine Components...
         /*   pc = bb.em
                    .getComponent(dogID, PhysicsComponent.class);
            pos = pc.getPosition();
            */
        }

        @Override
        public State onEvaluate(float delta) {
            //wird jeden Frame ausgeführt. 
            System.out.println("Evaluate, timer: "+ timer);
            State rueckgabe = State.FAILURE;
           // current = pc.getPosition();
            

            
            //Wenn die Zeit abgelaufen ist...
            

            
            if (timer < 0f){
                timer = MAX_TIME*2;
                System.out.println("zwei bis null "+ timer);
                pc = bb.em
                        .getComponent(dogID, PhysicsComponent.class);
                pos = pc.getPosition();
                
            }else{
            
        
            
                if(timer < MAX_TIME){
                    System.out.println("Vier bis zwei "+ timer);
                    //warum löst vier bis zwei ab zwei abwärts aus?
                   // timer = MAX_TIME;
                    
                    pcNext = bb.em
                            .getComponent(dogID, PhysicsComponent.class);
                    posNext = pcNext.getPosition();
                    
                    //current = pc.getPosition();
                    //Wenn die Position immer noch annährend gleich ist
                    
                    //---
                    //!!!Folgendes kann zu wirrem rumrennen führen, wenn das Epsilon nicht gut angepasst ist und zu genau!!!
                    //---
                    if(posNext.epsilonEquals(pos, 1f)){
                       // System.out.println("Position immer noch annährend gleich. posNext: " + posNext.x + " , " + posNext.y +". Pos: "+ pos.x + ", "+ pos.y);
                        rueckgabe = State.SUCCESS;
                        
                     }else {
                         
                     
                         rueckgabe = State.FAILURE;
                      //   System.out.println("Position differenziert zu letztem mal. "+ + posNext.x + " , " + posNext.y +". Pos: "+ pos.x + ", "+ pos.y);                 
                         } 
                    }     
            
                
            }
            timer -= delta;
            return rueckgabe; //nur Success, wenn der Hund hängt.
        }
    }
    
    class HundInRandomRichtung extends BaseTask{
        //Hund geht in aktuelle Richtung plus Abweichung von 1-3Körperlängen
        double high, low;
        //Man hätte eigendlich noch eine zwischen-Klasse in die Vererbung fügen müssen zwischen PhysicsComponent und Cat-/DogPhysicsComponent
        CatPhysicsComponent cpc;
        DogPhysicsComponent dpc;
        float timer;
        float MAX_TIME = 2;
        
        public HundInRandomRichtung(BaseNode parent) {
            super(parent);
            timer = MAX_TIME;
            x = (float) (Math.random() * (high - low) + low);
            y = (float) (Math.random() * (high - low) + low);
            if(pc instanceof CatPhysicsComponent){
                cpc = (CatPhysicsComponent) pc;
            }
              
            if (pc instanceof DogPhysicsComponent){
                dpc = (DogPhysicsComponent) pc;
            }
            
        }

        @Override
        public State onRun(float delta) {
            
           high = 0; low = 0;
            //Hund geht in Richtung: Aktuelle Richtung plus irgendwas zwischen 1-3Körperlängen
           if(pc instanceof CatPhysicsComponent){
               low = cpc.height;
               high = cpc.height*3;   
           }
             
           if (pc instanceof DogPhysicsComponent){
               low = dpc.mHeight;
               high = dpc.mHeight*3;   
           }
             
           if(timer < 0f){
               timer = MAX_TIME;
               ic.whereToGo = pc.getPosition().add(x, y);    
           }
           
            timer -= delta;
                                   
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
    
    

    class ChaseCat extends BaseTask {

        public ChaseCat(BaseNode parent) {
            super(parent);
            //Im Konstruktor gibt es noch keine Components
       /*     cat = bb.em.getAllEntitiesWithComponents(
                    PlayerComponent.class, PhysicsComponent.class);
            cpc = bb.em.getComponent(cat.first(),
                    PhysicsComponent.class);
            */
        }

        @Override
        public State onRun(float delta) {
            cat = bb.em.getAllEntitiesWithComponents(
                    PlayerComponent.class, PhysicsComponent.class);
            cpc = bb.em.getComponent(cat.first(),
                    PhysicsComponent.class);
            Vector2 pos = cpc.getPosition();

            InputComponent dog = bb.em
                    .getComponent(dogID, InputComponent.class);
            dog.whereToGo.set(pos);
            //logger.debug("Dog is Chasing!");
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

    // Das folgende komplexe Verhalten folgt ggf später, drin lassen:
    /*
     * 
     * class KatzeSichtbar extends BaseCondition{ //ist die Katze sichtbar?
     * public KatzeSichtbar(BaseNode parent, Behaviour behaviour) {
     * super(parent, behaviour); }
     * 
     * public State onEvaluate(float delta) { //---- //TODO sobald möglich
     * //SUCCESS zurückgeben wenn Katze sichtbar, sonst FAILURE. State result =
     * State.SUCCESS; return result; }
     * 
     * 
     * }
     * 
     * class BBLeer extends BaseCondition{ //ist das BlackBoard leer? public
     * BBLeer(BaseNode parent, Behaviour behaviour) { super(parent, behaviour);
     * }
     * 
     * public State onEvaluate(float delta) {
     * 
     * Blackboard b = ((Blackboard) behaviour.getLocalBlackboard()); //----
     * //TODO wenn Blackboard leer -> success sonst failure. //___________ State
     * result = State.SUCCESS; return result; }
     * 
     * }
     * 
     * class BBschreiben extends BaseTask{
     * 
     * public BBschreiben(BaseNode parent) { super(parent);
     * 
     * }
     * 
     * @Override public State onRun(float delta) { // TODO Auto-generated method
     * stub return null; }
     * 
     * @Override public void onActivate() { // TODO Auto-generated method stub
     * 
     * }
     * 
     * @Override public void onDeactivate() { // TODO Auto-generated method stub
     * 
     * }
     * 
     * }
     */

    public static class DogBlackboard {
        EntityManager em;
        public DogBlackboard(EntityManager em) {
            // TODO Auto-generated constructor stub
            this.em = em;
        }
    }

}
