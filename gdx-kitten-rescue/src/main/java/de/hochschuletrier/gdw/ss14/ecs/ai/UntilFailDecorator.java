package de.hochschuletrier.gdw.ss14.ecs.ai;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseCondition;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.nodes.BaseNode.State;

//Extends muss noch durch BaseDecorator ersetzt werden o.Ä.
public class UntilFailDecorator extends BaseCondition{

    public UntilFailDecorator(BaseNode parent, Behaviour behaviour)
    {
        super(parent, behaviour);
    }
 
    public State onEvaluate(float delta)
    {
           //result noch nur dummy:
           State result = State.SUCCESS;
           return result;
    }
    
    
}
