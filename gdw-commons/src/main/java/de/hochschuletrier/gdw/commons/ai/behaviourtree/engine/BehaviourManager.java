package de.hochschuletrier.gdw.commons.ai.behaviourtree.engine;

import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BehaviourManager {
    private static final Logger logger = LoggerFactory.getLogger(BehaviourManager.class);

    private LinkedList<Behaviour> behaviours;
    private int finishedBehaviours;
    private Object globalBlackboard;
    private boolean isRunning = true;

    public BehaviourManager(Object globalBlackboard) {
        behaviours = new LinkedList<Behaviour>();
        this.globalBlackboard = globalBlackboard;
    }

    public void update(float delta) {
        if (isRunning && !isFinished()) {
            for (Behaviour tree : behaviours) {
                tree.update(delta);
            }
        }
    }

    public void activate() {
        logger.debug("Starting all behaviours.");
        for (Behaviour b : behaviours) {
            b.activate();
        }
    }

    public void addBehaviour(Behaviour b) {
        b.setGlobalBlackboard(globalBlackboard);
        b.setManager(this);
        behaviours.add(b);
    }

    public void treeFinished(Behaviour t) {
        if ((++finishedBehaviours) == behaviours.size()) {
            logger.debug("All behaviours finished regularly.");
        }
    }

    public Object getGlobalBlackboard() {
        return globalBlackboard;
    }

    public void setGlobalBlackboard(Object globalBlackboard) {
        this.globalBlackboard = globalBlackboard;
    }

    public void deactivate() {
        for (Behaviour b : behaviours) {
            b.deactivate();
        }
        logger.debug("Engine stopped.");
    }

    public void pause() {
        isRunning = false;
        logger.debug("Engine paused.");
    }

    public void resume() {
        isRunning = true;
        logger.debug("Engine resumed.");
    }

    public void reset() {
        deactivate();
        activate();
    }

    public boolean isFinished() {
        return finishedBehaviours == behaviours.size();
    }

    public boolean isRunning() {
        return isRunning;
    }
}
