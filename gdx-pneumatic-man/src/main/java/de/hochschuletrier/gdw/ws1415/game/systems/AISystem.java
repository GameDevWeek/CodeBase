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

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        DirectionComponent direction = entity.getComponent(DirectionComponent.class);
        AIComponent ai = ComponentMappers.AI.get(entity);
        // TODO: ask for unit state → go on if unit is at bottom

        int tilesize = GameConstants.getTILESIZE();

        if(ai.type == AIType.PASSIVE);

        Ray ray = new Ray();
        this.physixSystem.getWorld().rayCast(ray, physix.getPosition(),
                physix.getPosition()
                        .add(direction.facingDirection.toVector2().scl(tilesize))
                        .sub(Vector2.Y.scl(tilesize*0.5f))
        );

        if(ray.fraction*ray.fraction <= 1.25f*tilesize*tilesize){
            ray.fixture.getBody();
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
