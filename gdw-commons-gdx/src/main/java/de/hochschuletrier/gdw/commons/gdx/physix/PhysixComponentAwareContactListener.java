package de.hochschuletrier.gdw.commons.gdx.physix;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixComponentAwareContactListener implements ContactListener {

    private final PhysixContact physixContact = new PhysixContact();

    private final LinkedHashMap<Class<? extends Component>, ComponentContactListener> listenerMap = new LinkedHashMap();

    public void addListener(Class<? extends Component> clazz, PhysixContactListener listener) {
        ComponentContactListener componentListener = listenerMap.get(clazz);
        if (componentListener == null) {
            componentListener = new ComponentContactListener(clazz);
            listenerMap.put(clazz, componentListener);
        }
        componentListener.listeners.add(listener);
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
            for (ComponentContactListener listener : listenerMap.values()) {
                listener.beginContact(contact);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
            for (ComponentContactListener listener : listenerMap.values()) {
                listener.endContact(contact);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
            for (ComponentContactListener listener : listenerMap.values()) {
                listener.preSolve(contact, oldManifold);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
            for (ComponentContactListener listener : listenerMap.values()) {
                listener.postSolve(contact, impulse);
            }
        }
    }

    private class ComponentContactListener {

        public final ComponentMapper mapper;
        public final HashSet<PhysixContactListener> listeners = new HashSet();

        public ComponentContactListener(Class<? extends Component> componentClass) {
            mapper = ComponentMapper.getFor(componentClass);
        }

        public void beginContact(Contact contact) {
            testAndRun(contact, (PhysixContact physixContact) -> beginContact(physixContact));
        }

        public void beginContact(PhysixContact contact) {
            listeners.forEach((PhysixContactListener listener) -> listener.endContact(physixContact));
        }

        public void endContact(Contact contact) {
            testAndRun(contact, (PhysixContact physixContact) -> endContact(physixContact));
        }

        public void endContact(PhysixContact contact) {
            listeners.forEach((PhysixContactListener listener) -> listener.endContact(physixContact));
        }

        public void preSolve(Contact contact, Manifold oldManifold) {
            testAndRun(contact, (PhysixContact physixContact) -> preSolve(physixContact, oldManifold));
        }

        public void preSolve(PhysixContact contact, Manifold oldManifold) {
            listeners.forEach((PhysixContactListener listener) -> listener.preSolve(physixContact, oldManifold));
        }

        public void postSolve(Contact contact, ContactImpulse impulse) {
            testAndRun(contact, (PhysixContact physixContact) -> postSolve(physixContact, impulse));
        }

        public void postSolve(PhysixContact contact, ContactImpulse impulse) {
            listeners.forEach((PhysixContactListener listener) -> listener.postSolve(contact, impulse));
        }

        private void testAndRun(Contact contact, Consumer<PhysixContact> consumer) {
            physixContact.set(contact);

            if (!testAndRun2(consumer)) {
                physixContact.swap();
                testAndRun2(consumer);
            }
        }
        
        private boolean testAndRun2(Consumer<PhysixContact> consumer) {
            PhysixBodyComponent myComponent = physixContact.getMyComponent();
            if(myComponent != null) {
                Entity entity = myComponent.getEntity();
                Component component = mapper.get(entity);
                if (component != null) {
                    consumer.accept(physixContact);
                    return true;
                }
            }
            return false;
        }
    }
}
