package de.hochschuletrier.gdw.ws1415.sandbox.maptest;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
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
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.BouncingComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.MovementSystem;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class MovementTest extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(MovementTest.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE, GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE, GameConstants.COMPONENT_POOL_MAX_SIZE
    );
    private final PhysixSystem physixSystem = new PhysixSystem(GameConstants.BOX2D_SCALE,
            GameConstants.VELOCITY_ITERATIONS, GameConstants.POSITION_ITERATIONS, GameConstants.PRIORITY_PHYSIX
    );
    private final PhysixDebugRenderSystem physixDebugRenderSystem = new PhysixDebugRenderSystem(GameConstants.PRIORITY_DEBUG_WORLD);
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private float totalMapWidth, totalMapHeight;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private PhysixBodyComponent playerBody;
    private MovementComponent movementComponent;
    private BouncingComponent bouncingComponent;
    private JumpComponent jumpComponent;
    
    private MovementSystem movementSystem = new MovementSystem(50);
    private final HashMap<TileSet, Texture> tilesetImages = new HashMap();

    public MovementTest() {
        engine.addSystem(physixSystem);
        engine.addSystem(physixDebugRenderSystem);
        engine.addSystem(movementSystem);
    }

    @Override
    public void init(AssetManagerX assetManager) {
        map = loadMap("data/maps/Test_Physics.tmx");
        for (TileSet tileset : map.getTileSets()) {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
        mapRenderer = new TiledMapRendererGdx(map, tilesetImages);

        // Generate static world
        int tileWidth = map.getTileWidth();
        int tileHeight = map.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(rect, tileWidth, tileHeight));
        generateWorldFromTileMap();
        
        //Create a SpawnPoint
        Entity spawn = engine.createEntity();
        PositionComponent spawnPoint = engine.createComponent(PositionComponent.class);
        spawnPoint.x = 200;
        spawnPoint.y = 100;
        SpawnComponent spawnflag = engine.createComponent(SpawnComponent.class);
        spawnflag.reset();
        spawn.add(spawnflag);
        spawn.add(spawnPoint);
        
        engine.addEntity(spawn);

        // create a simple player ball
        Entity player = engine.createEntity();
        PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        player.add(modifyComponent);

        modifyComponent.schedule(() -> {
            playerBody = engine.createComponent(PhysixBodyComponent.class);
            PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(spawn.getComponent(PositionComponent.class).x,
            		spawn.getComponent(PositionComponent.class).y).fixedRotation(true);
            playerBody.init(bodyDef, physixSystem, player);
            PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0f).restitution(0.0001f).shapeBox(58, 90);
            playerBody.createFixture(fixtureDef);
            player.add(playerBody);
        });
        
        //=========================== MOVEMENT TEST
        physixSystem.setGravity(0, 24);
        
        movementComponent = new MovementComponent();
        movementComponent.speed = 15000.0f;
        player.add(movementComponent);
        
        jumpComponent = new JumpComponent(100000.0f, 0.02f);
        player.add(jumpComponent);

        engine.addEntity(player);
        // ==============================================
        // Setup camera
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);
    }

    private void addShape(Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem).position(x, y).fixedRotation(false);
        Body body = physixSystem.getWorld().createBody(bodyDef);
        body.createFixture(new PhysixFixtureDef(physixSystem).density(1).friction(0.5f).shapeBox(width, height));
    }

    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(camera);
        tilesetImages.values().forEach(Texture::dispose);
    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
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

    @Override
    public void update(float delta) {
        camera.bind();
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }
        engine.update(delta);
        
        if(movementComponent.movingLeft){
        	movementComponent.stopMovingLeft();
        }
        if(movementComponent.movingRight){
        	movementComponent.stopMovingRight();
        }
        
        
        mapRenderer.update(delta);
        camera.update(delta);

        if(playerBody != null) {
           
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                movementComponent.moveLeft();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            	movementComponent.moveRight();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                jumpComponent.jump();
            }
            
            camera.setDestination(playerBody.getPosition());
        }
    }
}
