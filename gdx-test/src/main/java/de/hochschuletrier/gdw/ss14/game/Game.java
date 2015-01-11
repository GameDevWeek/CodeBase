package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
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
import de.hochschuletrier.gdw.ss14.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ss14.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ss14.game.contactlisteners.TriggerListener;
import de.hochschuletrier.gdw.ss14.game.systems.AnimationRenderSystem;
import de.hochschuletrier.gdw.ss14.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ss14.game.utils.PhysixUtil;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game extends InputAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final AnimationRenderSystem animationRenderSystem = new AnimationRenderSystem(GameConstants.PRIORITY_ANIMATIONS);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);

    private Sound impactSound;
    private AnimationExtended ballAnimation;

    public Game() {
    }

    public void init(AssetManagerX assetManager) {
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar)->physixDebugRenderSystem.setProcessing(physixDebug.get()));
        
        impactSound = assetManager.getSound("click");
        ballAnimation = assetManager.getAnimation("ball");

        addSystems();
        addContactListeners();
        setupPhysixWorld();

        Main.inputMultiplexer.addProcessor(this);
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(animationRenderSystem);
        engine.addSystem(updatePositionSystem);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 24);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(410, 800).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(800, 20));
        PhysixUtil.createHollowCircle(physixSystem, 180, 180, 150, 30, 6);

        createTrigger(410, 1000, 1600, 40, (Entity entity) -> {
            engine.removeEntity(entity);
        });
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        engine.update(delta);
    }

    public void createTrigger(float x, float y, float width, float height, Consumer<Entity> consumer) {
        Entity entity = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        entity.add(modifyComponent);

        TriggerComponent triggerComponent = engine.createComponent(TriggerComponent.class);
        triggerComponent.consumer = consumer;
        entity.add(triggerComponent);

        modifyComponent.schedule(() -> {
            PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.StaticBody, physixSystem).position(x, y);
            bodyComponent.init(bodyDef, physixSystem, entity);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).sensor(true).shapeBox(width, height);
            bodyComponent.createFixture(fixtureDef);
            entity.add(bodyComponent);
        });
        engine.addEntity(entity);
    }

    public void createBall(float x, float y, float radius) {
        Entity entity = engine.createEntity();
        entity.add(engine.createComponent(PositionComponent.class));
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
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
            bodyComponent.applyImpulse(0, 50000);
        });
        engine.addEntity(entity);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        createBall(screenX, screenY, 30);
        return true;
    }
}
