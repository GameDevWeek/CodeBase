package de.hochschuletrier.gdw.commons.gdx.physix.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactListener;
import de.hochschuletrier.gdw.commons.utils.Point;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author Santo Pfingsten
 */
public class PhysixSystem extends EntitySystem {

    public final float scale, scaleInv;
    protected final World world;
    protected final Vector2 gravity = new Vector2();
    protected final PhysixContact physixContact = new PhysixContact();

    private final LinkedHashMap<Class<Component>, ComponentContactListener> listenerMap = new LinkedHashMap();

    public PhysixSystem(float scale, float gravityX, float gravityY) {
        this.scale = scale;
        scaleInv = 1.0f / scale;
        gravity.set(gravityX, gravityY);
        world = new World(gravity, true);

        world.setContactListener(new InternalContactListener());
    }
    
    public void addComponentContactListener(Class<Component> clazz, PhysixContactListener listener) {
        ComponentContactListener componentListener = listenerMap.get(clazz);
        if(componentListener == null) {
            componentListener = new ComponentContactListener(clazz);
            listenerMap.put(clazz, componentListener);
        }
        componentListener.listeners.add(listener);
    }

    public void update(float timeStep, int velocityIterations, int positionIterations) {
        world.step(timeStep, velocityIterations, positionIterations);
        world.clearForces();
    }

    public void reset() {
        if (world.isLocked()) {
            throw new GdxRuntimeException("PhysixManager.reset called in locked state");
        }

        Array<Body> bodies = new Array();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }

        Array<Joint> joints = new Array();
        world.getJoints(joints);
        for (Joint joint : joints) {
            world.destroyJoint(joint);
        }
    }

    public World getWorld() {
        return world;
    }

    public void destroy(PhysixBody body) {
        world.destroyBody(body.getBody());
    }

    public void ropeConnect(PhysixBody a, PhysixBody b, float length) {
        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.bodyA = a.getBody();
        ropeJointDef.bodyB = b.getBody();
        ropeJointDef.maxLength = length * scale;
        ropeJointDef.collideConnected = true;
        world.createJoint(ropeJointDef);
    }

    /**
     * Convert world to box2d coordinates
     */
    public float toBox2D(float pixel) {
        return pixel * scaleInv;
    }

    /**
     * Convert box2d to world coordinates
     */
    public float toWorld(float num) {
        return num * scale;
    }

    /**
     * Convert world to box2d coordinates
     */
    public Vector2 toBox2D(float x, float y, Vector2 out) {
        out.set(x * scaleInv, y * scaleInv);
        return out;
    }

    /**
     * Convert world to box2d coordinates
     */
    public Vector2 toBox2D(Vector2 in, Vector2 out) {
        out.set(in.x * scaleInv, in.y * scaleInv);
        return out;
    }

    /**
     * Convert box2d to world coordinates
     */
    public Vector2 toWorld(Vector2 in, Vector2 out) {
        out.set(in.x * scale, in.y * scale);
        return out;
    }

    public Vector2[] toBox2D(List<Point> pointList) {
        Vector2[] returner = new Vector2[pointList.size()];
        for (int pointCount = 0; pointCount < returner.length; pointCount++) {
            Point p = pointList.get(pointCount);
            returner[pointCount] = new Vector2(p.x * scale, p.y * scale);
        }
        return returner;
    }

    public void setGravity(float x, float y) {
        gravity.set(x, y);
        world.setGravity(gravity);
        Array<Body> bodies = new Array();
        world.getBodies(bodies);
        for (Body body : bodies) {
            body.setAwake(true);
        }
    }

    private class InternalContactListener implements ContactListener {

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

            Entity owner = physixContact.getMyPhysixBody().getOwner();
            Component component = mapper.get(owner);
            if (component != null) {
                consumer.accept(physixContact);
            } else {
                physixContact.swap();
                owner = physixContact.getMyPhysixBody().getOwner();
                component = mapper.get(owner);
                if (component != null) {
                    consumer.accept(physixContact);
                }
            }
        }
    }
}
