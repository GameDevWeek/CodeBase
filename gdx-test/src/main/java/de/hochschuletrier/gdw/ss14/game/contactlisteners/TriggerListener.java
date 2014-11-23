package de.hochschuletrier.gdw.ss14.game.contactlisteners;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ss14.game.ComponentMappers;
import de.hochschuletrier.gdw.ss14.game.components.TriggerComponent;

public class TriggerListener extends PhysixContactAdapter {

    public void beginContact(PhysixContact contact) {
        PhysixBodyComponent otherComponent = contact.getOtherComponent();
        if (otherComponent != null) {
            TriggerComponent tc = ComponentMappers.trigger.get(contact.getMyComponent().getEntity());
            if (tc != null && tc.consumer != null) {
                tc.consumer.accept(otherComponent.getEntity());
            }
        }
    }
}
