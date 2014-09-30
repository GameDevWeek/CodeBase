package de.hochschuletrier.gdw.ss14.sandbox.tiledboxes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Santo Pfingsten
 */
public class TiledBoxes extends SandboxGame {

    private static final Logger logger = LoggerFactory.getLogger(TiledBoxes.class);

    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    PhysixManager manager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private float totalMapWidth, totalMapHeight;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;
    private PhysixBody playerBody;

    public TiledBoxes() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        map = loadMap("data/maps/demo.tmx");
        HashMap<TileSet, Texture> tilesetImages = new HashMap();
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
                (Layer layer, TileInfo info)->info.getBooleanProperty("blocked", false),
                (Rectangle rect)->addShape(rect, tileWidth, tileHeight));
        
        // create a simple player ball
        playerBody = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, manager).position(100, 100)
                .fixedRotation(true).create();
        playerBody.createFixture(new PhysixFixtureDef(manager).density(5).friction(0.2f).restitution(0.4f).shapeCircle(30));
        
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

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, manager).position(x, y)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(manager).density(1).friction(0.5f).shapeBox(width, height));
    }

    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(camera);
    }

    public TiledMap loadMap(String filename) {
        try {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    @Override
    public void render() {
        camera.bind();
        for (Layer layer : map.getLayers()) {
            mapRenderer.render(0, 0, layer);
        }
        manager.render();
    }

    @Override
    public void update(float delta) {
        manager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        mapRenderer.update(delta);
        camera.update(delta);
        
        float speed = 10000.0f;
        float velX=0, velY=0;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            velX -= delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            velX += delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            velY -= delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            velY += delta * speed;

        
        playerBody.setLinearVelocity(velX, velY);
        camera.setDestination(playerBody.getPosition());
    }
}
