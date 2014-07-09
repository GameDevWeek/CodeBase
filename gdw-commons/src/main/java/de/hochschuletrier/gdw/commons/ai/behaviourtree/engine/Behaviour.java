package de.hochschuletrier.gdw.commons.ai.behaviourtree.engine;

import java.util.ArrayList;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces.Leaf;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces.Root;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Behaviour extends BaseNode implements Root {
    private static final Logger logger = LoggerFactory.getLogger(Behaviour.class);

    private Object localBlackboard;
    private Object globalBlackboard;
    private String name;
    private BehaviourManager manager;
    private BaseNode child;
    private boolean isLooping = false;
    private boolean isRunning = true;
    private ArrayList<Leaf> activeTasks = new ArrayList(),
            removeTasks = new ArrayList(), addTasks = new ArrayList();

    public Behaviour() {
        super(null);
    }

    public Behaviour(String name, Object localBlackboard, boolean isLooping) {
        super(null);
        this.localBlackboard = localBlackboard;
        setLooping(isLooping);
    }

    public final void update(float delta) {
        if (isRunning) {
            activeTasks.removeAll(removeTasks);
            activeTasks.addAll(addTasks);
            addTasks.clear();
            removeTasks.clear();
            for (Leaf t : activeTasks) {
                t.run(delta);
            }
        }
    }

    @Override
    public final void addTask(Leaf t) {
        addTasks.add(t);
    }

    @Override
    public final void deleteTask(Leaf t) {
        removeTasks.add(t);
    }

    @Override
    public final void addChild(BaseNode child) {
        setRoot(child);
    }

    public final void setRoot(BaseNode child) {
        if (this.child != null) {
            logger.warn("Behaviour {} : Changing root of BehaviourTree!", name);
        }
        this.child = child;
    }

    @Override
    public final void childTerminated(BaseNode child, State state) {
        deactivate();
        if (isLooping()) {
            activate();
        } else {
            logger.debug("Behaviour {} finished regularly.", name);
            manager.treeFinished(this);
        }
    }

    @Override
    public final void activate() {
        logger.debug("Behaviour {} activated.", name);
        if (child != null) {
            child.activate();
        }
    }

    @Override
    public final void deactivate() {
        logger.debug("Behaviour {} deactivated.", name);
        if (child != null) {
            child.deactivate();
        }
    }

    public void pause() {
        isRunning = false;
        logger.debug("Behaviour {} paused.", name);
    }

    public void resume() {
        isRunning = true;
        logger.debug("Behaviour {} resumed.", name);
    }

    public void reset() {
        deactivate();
        activate();
        logger.debug("Behaviour {} reset.", name);
    }

    public final Object getLocalBlackboard() {
        return localBlackboard;
    }

    public final void setLocalBlackboard(Object localBlackboard) {
        this.localBlackboard = localBlackboard;
    }

    public final Object getGlobalBlackboard() {
        return globalBlackboard;
    }

    public final void setGlobalBlackboard(Object globalBlackboard) {
        this.globalBlackboard = globalBlackboard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLooping() {
        return isLooping;
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }

    public BehaviourManager getManager() {
        return manager;
    }

    public void setManager(BehaviourManager manager) {
        this.manager = manager;
    }

}
