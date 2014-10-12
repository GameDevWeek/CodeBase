package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.interfaces.Leaf;
//BaseCondition ist ein spezielles Blatt zum Auswerten einfacher Bedingungen.

public abstract class BaseCondition extends BaseNode implements Leaf {

    public BaseCondition(BaseNode parent) {
        super(parent);
    }

    @Override
    public final void run(float delta) {
        State result = onEvaluate(delta);
        switch (result) {
            case SUCCESS:
                parent.childTerminated(this, State.SUCCESS);
                break;
            case FAILURE:
                deactivate();
                parent.childTerminated(this, State.FAILURE);
                break;
            default:
                break;
        }
    }

    public abstract State onEvaluate(float delta);

    @Override
    public final void addChild(BaseNode child) {
    }

    @Override
    public final void childTerminated(BaseNode child, State state) {
    }

    @Override
    public final void activate() {
        behaviour.addTask(this);
    }

    @Override
    public final void deactivate() {
        behaviour.deleteTask(this);
    }

}
