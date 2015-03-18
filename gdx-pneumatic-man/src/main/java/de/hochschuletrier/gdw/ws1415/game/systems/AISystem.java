package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.AIComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DirectionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.KillsPlayerOnContactComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.utils.AIType;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

public class AISystem extends IteratingSystem {

    public AISystem(PhysixSystem physixSystem) {
        this(0, physixSystem);
    }

    private PhysixSystem physixSystem;

    public AISystem(int priority, PhysixSystem physixSystem) {
        super(Family.all(AIComponent.class).get(), priority);
        this.physixSystem = physixSystem;
    }

    /**
     * @return  True if theres no block in front of the entity, and false if there is a block so the entity cant move on
     */
    private boolean checkInFront(PhysixBodyComponent physix, Vector2 dir, float speed){
        Ray ray = new Ray();
        this.physixSystem.getWorld().rayCast(ray, physix.getPosition(),
                physix.getPosition()
                        .add(dir.scl(speed)));

        if(ray.fraction <= speed) {
            if(ray.fixture.getBody().getUserData() != null &&
                    ray.fixture.getBody().getUserData() instanceof PhysixBodyComponent){
                PhysixBodyComponent other = (PhysixBodyComponent)ray.fixture.getBody().getUserData();
                if(ComponentMappers.block.has(other.getEntity())){ return false; }
            }
            return false;
        }
        return true;
    }

    /**
     * @return  True if theres a block to walk on
     */
    private boolean checkBottomFront(PhysixBodyComponent physix, Vector2 dir, float speed){
        Ray ray = new Ray();
        this.physixSystem.getWorld().rayCast(ray, physix.getPosition(),
                physix.getPosition()
                        .add(dir.scl(speed))
                        .sub(Vector2.Y.scl(physix.getY()))
        );

        if(ray.fraction <= speed) {
            if(ray.fixture.getBody().getUserData() != null &&
                    ray.fixture.getBody().getUserData() instanceof PhysixBodyComponent){
                PhysixBodyComponent other = (PhysixBodyComponent)ray.fixture.getBody().getUserData();
                if(ComponentMappers.block.has(other.getEntity())){
                    if(other.getEntity().getComponent(KillsPlayerOnContactComponent.class) == null)
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        DirectionComponent direction = entity.getComponent(DirectionComponent.class);
        AIComponent ai = ComponentMappers.AI.get(entity);
        if(ai.type == AIType.PASSIVE); //TODO: do some stuff based on AIType

        Vector2 dir = direction.facingDirection.toVector2();

        if(checkInFront(physix, dir, 0)){ // TODO: replace 0 with jump-width (movement component)
            direction.facingDirection = Direction.fromVector2(dir.scl(-1));
        }else if(checkBottomFront(physix, dir, 0)){ // TODO: replace 0 with jump-width (movement component)
            // move forward
            
        }

    }

    private class Ray implements RayCastCallback{

        Fixture fixture;
        float fraction;

        @Override
        public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            this.fixture = fixture;
            this.fraction = fraction;
            return 0;
        }

    }
}
