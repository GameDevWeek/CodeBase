package de.hochschuletrier.gdw.commons.gdx.physix;

import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * @author Santo Pfingsten
 */
public abstract class AbstractPhysixComponent extends Component implements Poolable {

    protected PhysixBody physicsBody;

    @Override
    public void reset() {
        //fixme: is it really desirable to do this with the libgdx pooling mechanism ?
        physicsBody.setActive(false);
    }

    public abstract void initPhysics(PhysixSystem manager);

    public Vector2 getPosition() {
        return physicsBody.getPosition();
    }

    public Vector2 getVelocity() {
        return physicsBody.getLinearVelocity();
    }

    public void setVelocity(Vector2 velocity) {
        physicsBody.setLinearVelocity(velocity);
    }

    public void setVelocity(float x, float y) {
        physicsBody.setLinearVelocity(x, y);
    }

    public void setVelocityX(float x) {
        physicsBody.setLinearVelocityX(x);
    }

    public void setVelocityY(float y) {
        physicsBody.setLinearVelocityY(y);
    }
    
    public void setRotation(float r){
        physicsBody.setTransform(physicsBody.getPosition().x, physicsBody.getPosition().y, r);
    }
    
    public float getRotation() {
        return physicsBody.getAngle();
    }
}
