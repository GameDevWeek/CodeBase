package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.decorators;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;

public class UntilFail extends BaseDecorator {

    public UntilFail(BaseNode parent) {
        super(parent);
    }

    @Override
    public State onChildTerminated(BaseNode child, State state) {
        switch (state) {
            case SUCCESS:
                child.activate();
                break;
            default:
                return State.FAILURE;
        }
        return State.FAILURE;
    }
}
