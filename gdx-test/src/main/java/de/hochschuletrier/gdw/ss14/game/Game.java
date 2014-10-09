package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static final int ENTITY_POOL_INITIAL_SIZE = 32;
    private static final int ENTITY_POOL_MAX_SIZE = 256;
    private static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    private static final int COMPONENT_POOL_MAX_SIZE = 256;

    private final PooledEngine engine = new PooledEngine(ENTITY_POOL_INITIAL_SIZE, ENTITY_POOL_MAX_SIZE, COMPONENT_POOL_INITIAL_SIZE, COMPONENT_POOL_MAX_SIZE);

    private static final int POSITION_ITERATIONS = 3;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final float STEP_SIZE = 1 / 30.0f;
    private static final int BOX2D_SCALE = 40;

    private final PhysixSystem physixSystem = new PhysixSystem(BOX2D_SCALE, STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);

    public Game() {
    }

    public void init(AssetManagerX assetManager) {

    }

    public void update(float delta) {
    }

    public void render() {
    }

    public void createBall(float x, float y, float radius) {
        Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = new PhysixModifierComponent();
        entity.add(modifyComponent);

        modifyComponent.schedule(() -> {
            PhysixBodyComponent component = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem)
                    .position(x, y).fixedRotation(false);
            component.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                    .density(5).friction(0.2f).restitution(0.4f).shapeCircle(radius);
            component.createFixture(fixtureDef);
        });
    }
}
