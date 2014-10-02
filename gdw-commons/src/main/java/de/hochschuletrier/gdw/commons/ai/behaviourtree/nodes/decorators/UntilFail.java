package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.decorators;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;



public class UntilFail extends BaseDecorator {


	public UntilFail(BaseNode parent, Behaviour tree) {
		super(parent, tree);
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
