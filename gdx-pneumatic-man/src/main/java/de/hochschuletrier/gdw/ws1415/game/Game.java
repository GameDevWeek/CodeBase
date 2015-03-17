package de.hochschuletrier.gdw.ws1415.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixComponentAwareContactListener;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixDebugRenderSystem;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.ImpactSoundListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.PlayerContactListener;
import de.hochschuletrier.gdw.ws1415.game.contactlisteners.TriggerListener;

import de.hochschuletrier.gdw.ws1415.game.systems.MovementSystem;

import de.hochschuletrier.gdw.ws1415.game.systems.AnimationRenderSubsystem;
import de.hochschuletrier.gdw.ws1415.game.systems.InputKeyboardSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.RenderSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.AISystem;
import de.hochschuletrier.gdw.ws1415.game.systems.UpdatePositionSystem;
import de.hochschuletrier.gdw.ws1415.game.utils.PhysixUtil;


import java.util.HashMap;
import java.util.function.Consumer;

public class Game {

    private final CVarBool physixDebug = new CVarBool("physix_debug", true, 0, "Draw physix debug");
    private final Hotkey togglePhysixDebug = new Hotkey(() -> physixDebug.toggle(false), Input.Keys.F1, HotkeyModifier.CTRL);

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );

    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final RenderSystem renderSystem = new RenderSystem(GameConstants.PRIORITY_ANIMATIONS);
    private final UpdatePositionSystem updatePositionSystem = new UpdatePositionSystem(GameConstants.PRIORITY_PHYSIX + 1);
    private final MovementSystem movementSystem = new MovementSystem(GameConstants.PRIORITY_PHYSIX+2);
    private final InputKeyboardSystem inputKeyboardSystem = new InputKeyboardSystem();
    private final AISystem aisystems = new AISystem(
            GameConstants.PRIORITY_PHYSIX + 1,
            physixSystem
            );

    private Sound impactSound;
    private AnimationExtended ballAnimation;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap();

    public Game() {
        // If this is a build jar file, disable hotkeys
        if (!Main.IS_RELEASE) {
            togglePhysixDebug.register();
        }
    }

    public void dispose() {
        togglePhysixDebug.unregister();
    }

    public void init(AssetManagerX assetManager) {
        Main.getInstance().console.register(physixDebug);
        physixDebug.addListener((CVar) -> physixDebugRenderSystem.setProcessing(physixDebug.get()));

        map = loadMap("data/maps/Test_Physics.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);

        setupPhysixWorld();
        generateWorldFromTileMap();

        //dummy
        createBall(500, 250, 50);

        addSystems();
        addContactListeners();
        
    
        Main.inputMultiplexer.addProcessor(inputKeyboardSystem);
        

        
    }

    private void generateWorldFromTileMap() {
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> {
                    return info.getBooleanProperty("Invulnerable", false)
                    && info.getProperty("Type", "").equals("Floor");
                },
                (Rectangle rect) -> {
                    EntityCreator.createAndAddInvulnerableFloor(engine,
                            physixSystem, rect, tileWidth, tileHeight);
                });

        for (Layer layer : map.getLayers()) {
            TileInfo[][] tiles = layer.getTiles();

            for (int i = 0; i < map.getWidth(); i++) {
                for (int j = 0; j < map.getHeight(); j++) {
                    if (tiles != null && tiles[i] != null && tiles[i][j] != null) {
                        if (tiles[i][j].getIntProperty("Hitpoint", 0) != 0
                                && tiles[i][j].getProperty("Type", "").equals("Floor")) {
                            EntityCreator.createAndAddVulnerableFloor(engine,
                                    physixSystem,
                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                    map.getTileWidth(),
                                    map.getTileHeight());
                        }
                    }
                }
            }
        }

    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    private void addSystems() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(updatePositionSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(inputKeyboardSystem);

        engine.addSystem(aisystems);
    }

    private void addContactListeners() {
        PhysixComponentAwareContactListener contactListener = new PhysixComponentAwareContactListener();
        physixSystem.getWorld().setContactListener(contactListener);
        contactListener.addListener(ImpactSoundComponent.class, new ImpactSoundListener());
        contactListener.addListener(TriggerComponent.class, new TriggerListener());
        contactListener.addListener(PlayerComponent.class, new PlayerContactListener());
    }

    private void setupPhysixWorld() {
        physixSystem.setGravity(0, 24);
    }

    public void update(float delta) {
        Main.getInstance().screenCamera.bind();
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }

        mapRenderer.update(delta);

        engine.update(delta);
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
        entity.add(engine.createComponent(LayerComponent.class));

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
}
