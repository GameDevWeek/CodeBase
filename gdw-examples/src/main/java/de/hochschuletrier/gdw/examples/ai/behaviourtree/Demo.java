package de.hochschuletrier.gdw.examples.ai.behaviourtree;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;

public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();
    }

    public Demo() {
        Blackboard globalBlackboard = new Blackboard();
        Blackboard localBlackboard = new Blackboard();
        localBlackboard.isAggressive = false;

        BehaviourManager bManager = new BehaviourManager(globalBlackboard);

        MyBehaviour myB = new MyBehaviour();
        myB.setLocalBlackboard(localBlackboard);

        bManager.addBehaviour(myB);
        bManager.activate();

        while (!bManager.isFinished()) {
            bManager.update(System.currentTimeMillis());
        }
    }

}
