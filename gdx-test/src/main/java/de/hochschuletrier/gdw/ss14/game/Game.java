package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss14.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss14.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss14.game.systems.AnimationRenderSystem;
import de.hochschuletrier.gdw.ss14.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss14.game.utils.PhysixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game extends InputAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static final int PRIORITY_PHYSIX = 0;
    private static final int PRIORITY_ENTITIES = 10;
    private static final int PRIORITY_ANIMATIONS = 20;
    private static final int PRIORITY_DEBUG_WORLD = 30;
    private static final int PRIORITY_HUD = 40;

    private static final int ENTITY_POOL_INITIAL_SIZE = 32;
    private static final int ENTITY_POOL_MAX_SIZE = 256;
    private static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    private static final int COMPONENT_POOL_MAX_SIZE = 256;

    private final PooledEngine engine = new PooledEngine(ENTITY_POOL_INITIAL_SIZE, ENTITY_POOL_MAX_SIZE, COMPONENT_POOL_INITIAL_SIZE, COMPONENT_POOL_MAX_SIZE);

    private static final int POSITION_ITERATIONS = 3;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final float STEP_SIZE = 1 / 30.0f;
    private static final int BOX2D_SCALE = 40;

    private final PhysixSystem physixSystem = new PhysixSystem(BOX2D_SCALE, STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PRIORITY_PHYSIX);
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(physixSystem.getWorld(), physixSystem.getScale(), PRIORITY_DEBUG_WORLD);
    private final AnimationRenderSystem animationRenderSystem = new AnimationRenderSystem(PRIORITY_ANIMATIONS);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(PRIORITY_PHYSIX + 1);

    private Sound impactSound;
    private AnimationExtended ballAnimation;

    public Game() {
    }

    public void init(AssetManagerX assetManager) {
        impactSound = assetManager.getSound("click");
        ballAnimation = assetManager.getAnimation("ball");

        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(animationRenderSystem);
        engine.addSystem(updatePositionSystem);
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());

        initWorld();

        Main.inputMultiplexer.addProcessor(this);
    }

    private void initWorld() {
        physixSystem.setGravity(0, 24);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(410, 400).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800, 20));
        PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        engine.update(delta);
    }

    public void createBall(float x, float y, float radius) {
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(PositionComponent.class));
        PhysixModifierComponent modifyComponent = new PhysixModifierComponent();
        entity.add(modifyComponent);

        ImpactSoundComponent soundComponent = engine.createComponent(ImpactSoundComponent.class);
        soundComponent.init(impactSound, 20, 20, 100);
        entity.add(soundComponent);

        AnimationComponent animComponent = engine.createComponent(AnimationComponent.class);
        animComponent.animation = ballAnimation;
        entity.add(animComponent);

        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem)
                    .position(x, y).fixedRotation(false);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                    .density(5).friction(0.2f).restitution(0.4f).shapeCircle(radius);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        createBall(screenX, screenY, 30);
        return true;
    }
}
