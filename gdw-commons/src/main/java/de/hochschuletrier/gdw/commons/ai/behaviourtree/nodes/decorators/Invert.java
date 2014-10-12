package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.decorators;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;

public class Invert extends BaseDecorator {

    public Invert(BaseNode parent) {
        super(parent);

    }

    @Override
    public State onChildTerminated(BaseNode child, State state) {
        switch (state) {
            case SUCCESS:
                return State.FAILURE;
            case FAILURE:
                return State.SUCCESS;
            default:
                break;
        }
        return null;
    }

}
