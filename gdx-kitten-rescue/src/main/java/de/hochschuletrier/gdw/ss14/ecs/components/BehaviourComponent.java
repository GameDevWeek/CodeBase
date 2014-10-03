package de.hochschuletrier.gdw.ss14.ecs.components;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.Behaviour;
import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.BehaviourManager;

public class BehaviourComponent implements Component {
Behaviour verhalten;
boolean dogKIAus;

public BehaviourComponent(Behaviour vh, BehaviourManager manager){
    verhalten = vh;
    dogKIAus = false;
    manager.addBehaviour(verhalten);
    
}

public BehaviourComponent(Behaviour vh, boolean kiAus){
    verhalten = vh;
    dogKIAus = kiAus;
    
}

public Behaviour getVerhalten() {
    return verhalten;
}

public void setVerhalten(Behaviour verhalten) {
    this.verhalten = verhalten;
}

public boolean isDogKIAus() {
    return dogKIAus;
}

public void setDogKIAus(boolean dogKIAus) {
    this.dogKIAus = dogKIAus;
}


    
}
