package de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.decorators;


import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode.State;

public abstract class BaseDecorator extends BaseNode {

	BaseNode child;

	public BaseDecorator(BaseNode parent ) {
		super(parent);
	}

	@Override
	public final void addChild(BaseNode child) {
		if (this.child != null) {
			throw new IllegalStateException(
					"Decorators can only have one child");
		}
		this.child = child;
	}

	@Override
	public final void childTerminated(BaseNode child, State state) {
		State result = onChildTerminated(child, state);
		switch (result) {
		case SUCCESS:
			deactivate();
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

	public abstract State onChildTerminated(BaseNode child, State state);

	@Override
	public final void activate() {
		if (child != null) {
			child.activate();
		} else {
			throw new NullPointerException(
					"Cannot activate subtree, there is no child. This"
							+ " node will idle until its deactivated!");
		}
	}

	@Override
	public final void deactivate() {
		if (child != null) {
			child.deactivate();
		} else {
			throw new NullPointerException(
					"Cannot deactivate subtree. There is no child to deactivate");

		}
	}
}
