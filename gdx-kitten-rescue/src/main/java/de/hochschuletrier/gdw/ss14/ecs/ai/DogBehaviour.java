package de.hochschuletrier.gdw.ss14.ecs.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.*;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.decorators.Invert;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.sound.SoundManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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
        
        /* Setup Blackboard informations */
        bb = (DogBlackboard) localBlackboard;
        ic = bb.em.getComponent(dogID, InputComponent.class);
        pc = bb.em.getComponent(dogID, PhysicsComponent.class);
        // muss schon Katze existieren...
        /*
         * cat = bb.em.getAllEntitiesWithComponents( PlayerComponent.class,
         * PhysicsComponent.class); cpc = bb.em.getComponent(cat.first(),
         * PhysicsComponent.class);
         */
        /* Setup Tree */
        
        /*
        //Finales Soll-Verhalten mit SeeCat, Patrouille, ChaseCat und Ecken ausweichen:
        setName("Catch the Cat, patrouillieren, katze sehen, und Ecken ausweichen.");
        BaseNode root = new Selector(this);
        Sequence hundHaengt = new Sequence(root);
        Selector jagen = new Selector(root);
        Selector hh = new Selector(hundHaengt);
        new HundInRandomRichtung(hundHaengt);
        new HundHaengt(hh);
        Invert dogNotChase = new Invert(hh);
        new DogIsChasing(dogNotChase);
        Sequence katzenChase = new Sequence(jagen);
        Sequence pat = new Sequence(jagen);
        new DogSeesCat(katzenChase);
        new ChaseCat(katzenChase);
        Invert nichtsehend = new Invert(pat);
        new Patroullieren(pat);
        new DogSeesCat(nichtsehend);
        */
        
        /*
        //SeeCat Patrouille oder Chase Cat verhalten:
        setName("Catch the Cat oder patroullieren");
        BaseNode root = new Selector(this);
        Sequence jageKatze = new Sequence(root);
        Sequence patroulliere = new Sequence(root);
        new DogSeesCat(jageKatze);
        new ChaseCat(jageKatze);
        Invert nichtsSehend = new Invert(patroulliere);
        new DogSeesCat(nichtsSehend);
        new Patroullieren(patroulliere);
        */
        
      //Katze verfolgen und ecken ausweichen verhalten:  
        setName("Catch the Cat und weich Ecken aus");
        BaseNode root = new Selector(this);
        Sequence hundHaengt = new Sequence(root);
        new DogBehaviour.ChaseCat(root);
        Selector hh = new Selector(hundHaengt);
        new HundInRandomRichtung(hundHaengt);
        Invert dogNotChase = new Invert(hh);
        new DogIsChasing(dogNotChase);
        new HundHaengt(hh);
        
        
        
     
   
        //Invert merke = new Invert(dogChase);
        //new DogBehaviour.HundHaengt(merke);

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

        public KIAus(BaseNode parent) {
            super(parent);

        }

        @Override
        public State onEvaluate(float delta) {

            State result = State.SUCCESS;
            return result;
        }

    }

    class HundHaengt extends BaseCondition {
        // Gibt Success zurück, wenn der Hund an einer Stelle hängt für eine
        // gewisse Zeit.
        float timer;
        float MAX_TIME = 2;
        Vector2 currentPos;
        Vector2 oldPos;

        boolean nochNichtGefeuert;
        PhysicsComponent pcNext;
        boolean zeitpunkt;

        public HundHaengt(BaseNode parent) {
            super(parent);
            nochNichtGefeuert = true;
            timer = MAX_TIME; 
            currentPos = new Vector2();
            oldPos = new Vector2();
            // Beim Konstruktor hat DogBehaviour noch keine Components...
            /*
             * pc = bb.em .getComponent(dogID, PhysicsComponent.class); pos =
             * pc.getPosition();
             */
        }

        @Override
        public State onEvaluate(float delta) {
            // wird jeden Frame ausgeführt.
            // logger.debug("HundHängt?");
            // System.out.println("Evaluate, timer: "+ timer);
            State rueckgabe = State.FAILURE;
            // current = pc.getPosition();

            // Wenn die Zeit abgelaufen ist...

            currentPos = new Vector2(pc.getPosition());
            pc = bb.em.getComponent(dogID, PhysicsComponent.class);

            if (oldPos.epsilonEquals(currentPos, 1f)) {
                // System.out.println("zwei bis null "+ timer);
                // System.out.println("Evaluate, timer: "+ timer);
                // System.out.println("zwei bis null, pos gesetzt zu Timer: "+timer+" pos: "+pos.x+", "+pos.y+", posNext: "+posNext.x+
                // ", "+posNext.y );
                if (timer < 0f) {
                    rueckgabe = State.SUCCESS;
                } else {
                    rueckgabe = State.FAILURE;
                }

                timer -= delta;
            } else {
                timer = MAX_TIME;
                rueckgabe = State.FAILURE;
            }

            oldPos.set(currentPos);

           // logger.debug("HundHeangt: Rückgabe:  " + rueckgabe + " INstanz: "
              //      + this);

            return rueckgabe; // nur Success, wenn der Hund hängt.

            /*
             * else {
             * 
             * if (timer < MAX_TIME && nochNichtGefeuert) { //
             * System.out.println("Vier bis zwei "+ timer); //
             * System.out.println("Evaluate, timer: "+ timer); nochNichtGefeuert
             * = false; // warum löst vier bis zwei ab zwei abwärts aus? //
             * timer = MAX_TIME;
             * 
             * pcNext = bb.em.getComponent(dogID, PhysicsComponent.class);
             * oldPos = new Vector2(pcNext.getPosition()); //
             * System.out.println(
             * "vier bis zwei, posNext gesetzt zu Timer: "+timer
             * +" posNext: "+posNext
             * .x+", "+posNext.y+", pos: "+pos.x+", "+pos.y+" Timer: "); //
             * current = pc.getPosition(); // Wenn die Position immer noch
             * annährend gleich ist
             * 
             * // --- // !!!Folgendes kann zu wirrem rumrennen führen, wenn das
             * // Epsilon nicht gut angepasst ist und zu genau!!! // --- if
             * (oldPos.epsilonEquals(pos, 1f)) {
             * //logger.debug("Position immer noch annährend gleich.?");//
             * posNext: // " + posNext.x + " // , // " + posNext.y +". // Pos:
             * // "+ pos.x + ", // "+ // pos.y); rueckgabe = State.SUCCESS;
             * 
             * } else { rueckgabe = State.FAILURE;
             * //logger.debug("Position differenziert zu letztem mal. "); // +
             * // + // posNext.x // + // " , " // + // posNext.y // +". Pos: "+
             * // pos.x // + // ", "+ // pos.y); } }
             * 
             * }
             */

        }
    }
    
    class DogIsChasing extends BaseCondition {
        /** gibt zurück ob der Hund am Jagen ist.*/
        boolean hundJagt;
        
        public DogIsChasing(BaseNode parent) {
            super(parent);
        }

        @Override
        public State onEvaluate(float delta) {
            DogPropertyComponent dpc = bb.em.getComponent(dogID, DogPropertyComponent.class);
            hundJagt = dpc.dogIsChasing;
            State rueckgabe = State.FAILURE;
            if(hundJagt)
                rueckgabe = State.SUCCESS;
            else
                rueckgabe = State.FAILURE;
            return rueckgabe;
        }
        
        
    }

    class DogSeesCat extends BaseCondition{

        public DogSeesCat(BaseNode parent) {
            super(parent);
        }

        @Override
        public State onEvaluate(float delta) {
            boolean hundSiehtKatze;
         
             EnemyComponent ec = bb.em.getComponent(dogID, EnemyComponent.class);
             hundSiehtKatze = ec.seeCat;
             System.out.println("Hund sieht Katze: "+hundSiehtKatze);
             State rueckgabe;
             if(hundSiehtKatze){
                 rueckgabe = State.SUCCESS;
                 SoundManager.performAction(state.SUCCESS);
             }
             else {
                 rueckgabe = State.FAILURE;
                 SoundManager.performAction(state.FAILURE);
             }
            return rueckgabe;
        }
        
        
    }
    
    class Patroullieren extends BaseTask {
        DogPropertyComponent proper;
        ArrayList<Vector2> patrolPunkte;
        int naechsterPatrolpunktIndex;
        Vector2 currentPos;
        
        public Patroullieren(BaseNode parent) {
            super(parent);
            naechsterPatrolpunktIndex = 0;
        }

        @Override
        public State onRun(float delta) {
           proper = bb.em.getComponent(dogID, DogPropertyComponent.class);
           patrolPunkte =  proper.patrolspots;
           int laenge = patrolPunkte.size();
           if (laenge <= 0){
               //Wenn keine Punkte da sind wird nichts getan.
           } else{
               //von i bis länge hochzählen
               ic.whereToGo = patrolPunkte.get(naechsterPatrolpunktIndex);
               currentPos = new Vector2(pc.getPosition());
               if(patrolPunkte.get(naechsterPatrolpunktIndex).epsilonEquals(currentPos, 1f)){
                   //nächsterPunkt, wenn man angekommen ist
                   if(patrolPunkte.size()< naechsterPatrolpunktIndex+1){
                       //dann kann man einfach den nächsten setzen (+1)
                       naechsterPatrolpunktIndex += 1;
                   } else {
                       //wieder vorne anfangen
                       naechsterPatrolpunktIndex = 0;
                   }
               }    
               ic.whereToGo = patrolPunkte.get(naechsterPatrolpunktIndex);
           }
               
           
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
    
    class HundInRandomRichtung extends BaseTask {
        // Hund geht in aktuelle Richtung plus Abweichung von 1-3Körperlängen
        double high, low;
        // Man hätte eigendlich noch eine zwischen-Klasse in die Vererbung fügen
        // müssen zwischen PhysicsComponent und Cat-/DogPhysicsComponent
        CatPhysicsComponent cpc;
        DogPhysicsComponent dpc;
        float timer;
        boolean timerLaeuft;
        float MAX_TIME = 2;

        public HundInRandomRichtung(BaseNode parent) {
            /**Muss erstellt werden nachdem Dog/CatPhysicsComponent angemeldet wurde.
             *  Soll: Zielpunkt wird zufällig rechts oder links rum um 90Grad um den Hund rotiert, um 1-3 Körperlängen (zufällig) verlängert*/
            super(parent);
            timer = MAX_TIME;
            timerLaeuft = false;
            //x, y sind die zufälligen Abweichungen in Körperlängen
            x = (float) (Math.random() * (high - low) + low);
            //es wird bei der neuen Implementierung nur eine abweichung für beide Werte benötigt.
           // y = (float) (Math.random() * (high - low) + low);
            if (pc instanceof CatPhysicsComponent) {
                cpc = (CatPhysicsComponent) pc;
                low = cpc.height;
                high = cpc.height * 3;
            }

            if (pc instanceof DogPhysicsComponent) {
                dpc = (DogPhysicsComponent) pc;
                low = dpc.mHeight;
                high = dpc.mHeight * 3;
            }

        }

        @Override
        public State onRun(float delta) {
            //Vorgehen: 
            //1. Zielpunkt um 90 Grad um Hund rotieren. Zufällig ob rechts oder links rum
            //2. Zielpunkt in die richtung analog zu vorheriger zufallsrichtung um 1-3 Körperlängen verlängern
            Vector2 aktuellerZielpunkt;
            Vector2 positionDesHundes;
            Vector2 neuesZiel= new Vector2();
            DogPropertyComponent dprc = bb.em.getComponent(dogID, DogPropertyComponent.class);
            
            if (timerLaeuft) {
            //nur Pointer!!!:
           positionDesHundes = pc.getPosition();
           aktuellerZielpunkt = ic.whereToGo;
           Vector2 entfernung;
           entfernung = new Vector2( (positionDesHundes.x-aktuellerZielpunkt.x), (positionDesHundes.y-aktuellerZielpunkt.y));           
           high = 10;
           low = 0;
           //random zwischen high und low:
           double zufall = (Math.random() * (high - low) + low);
           //-------------
           //TODO: randbedingungen, falls der Zielpunkt ausserhalb der Karte ist gibt es fehler??? Oder ist das kein Problem???
           //-------------
           if(zufall < 5){
               //in der hälfte der Fälle sagt Laplace: "
               //plus
               neuesZiel.x = aktuellerZielpunkt.x + entfernung.y;
               neuesZiel.y = aktuellerZielpunkt.y + entfernung.x;
               
           } else {
               //minus
               neuesZiel.x = aktuellerZielpunkt.x - entfernung.y;
               neuesZiel.y = aktuellerZielpunkt.y - entfernung.x;
           }
           //2.: Wenn x von entfernung kleiner ist als y von entfernung dann x verlängern um zufällig 1-3 körperlängen.           
            // 1-3Körperlängen
            if (pc instanceof CatPhysicsComponent) {
                low = cpc.height;
                high = cpc.height * 3;
            }

            if (pc instanceof DogPhysicsComponent) {
                low = dpc.mHeight;
                high = dpc.mHeight * 3;
            }
            float verlaengerung = (float) (Math.random() * (high - low) + low);
            if(entfernung.x < entfernung.y)
            neuesZiel.x += verlaengerung;
            else
            neuesZiel.y += verlaengerung;
            }
            ic.whereToGo = neuesZiel;

            if (timer < 0f) {
                timer = MAX_TIME;
                ic.whereToGo = neuesZiel;
                timerLaeuft = true;
                dprc.dogIsChasing = false;
                
            }else {
                timerLaeuft = false;
                dprc.dogIsChasing = true;
            }
            timer -= delta;

            //logger.debug("HundInRandomRichtung: Rückgabe:  " + State.SUCCESS);
            // System.out.println("HundInRandomRichtung: Rückgabe:  " +
            // State.SUCCESS);
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
            // Im Konstruktor gibt es noch keine Components
            /*
             * cat = bb.em.getAllEntitiesWithComponents( PlayerComponent.class,
             * PhysicsComponent.class); cpc = bb.em.getComponent(cat.first(),
             * PhysicsComponent.class);
             */
        }

        @Override
        public State onRun(float delta) {
            cat = bb.em.getAllEntitiesWithComponents(PlayerComponent.class,
                    PhysicsComponent.class);
            cpc = bb.em.getComponent(cat.first(), PhysicsComponent.class);
            Vector2 pos = cpc.getPosition();

            InputComponent dog = bb.em
                    .getComponent(dogID, InputComponent.class);
            DogPropertyComponent dpc = bb.em.getComponent(dogID, DogPropertyComponent.class);
            dpc.dogIsChasing = true;
            dog.whereToGo.set(pos);
            // logger.debug("Dog is Chasing!");

          //  logger.debug("ChaseCat: Rückgabe:  " + State.SUCCESS);

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
