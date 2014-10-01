package de.hochschuletrier.gdw.ss14.ecs.ai;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseTask;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.RandomChoice;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DogBehaviour extends Behaviour {
    private static final Logger logger = LoggerFactory
            .getLogger(DogBehaviour.class);
    
    
    public DogBehaviour() {
        setName("Catch the Cat");
        BaseNode root = new Sequence(this);
  
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
    
    
}


/*
package de.hochschuletrier.gdw.examples.ai.behaviourtree;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseTask;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.RandomChoice;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyBehaviour extends Behaviour {
    private static final Logger logger = LoggerFactory
            .getLogger(MyBehaviour.class);

    public MyBehaviour() {
        setName("Log to Console");
        BaseNode root = new Sequence(this);
        Writer w = new Writer(root, "D");
        new Writer(root, "E");
        new Writer(root, "M");
        new Writer(root, "O");
        RandomChoice random = new RandomChoice(root);
        new Writer(random, "!");
        new Writer(random, "?");
        setLooping(false);

    }

    class Writer extends BaseTask {

        String string;

        public Writer(BaseNode parent, String string) {
            super(parent);
            this.string = string;
        }

        @Override
        public State onRun(float delta) {
            Blackboard b = ((Blackboard) getLocalBlackboard());
            if (b.isAggressive && (string.equals("!") || string.equals("?"))) {
                logger.info(string);
                logger.info(string);
                logger.info(string);
            }else{
                logger.info(string);
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
}

class Blackboard {

    Object player;
    Object[] enemies;
    boolean isAggressive = true;
}



*/