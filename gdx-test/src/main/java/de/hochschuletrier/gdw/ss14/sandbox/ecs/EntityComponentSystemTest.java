package de.hochschuletrier.gdw.ss14.sandbox.ecs;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.*;
import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.gdx.tiled.*;
import de.hochschuletrier.gdw.commons.resourcelocator.*;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.commons.tiled.tmx.*;
import de.hochschuletrier.gdw.commons.tiled.utils.*;
import de.hochschuletrier.gdw.commons.utils.*;
import de.hochschuletrier.gdw.ss14.sandbox.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.components.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.systems.*;

import java.util.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class EntityComponentSystemTest extends SandboxGame
{
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private Engine engine;
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private Texture texture;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;


    @Override
    public void init(AssetManagerX assetManager)
    {
        engine = new Engine();
        entityManager = new EntityManager();
        physixManager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);

        map = loadMap("data/maps/demo.tmx");
        HashMap<TileSet, Texture> tilesetImages = new HashMap();
        for (TileSet tileset : map.getTileSets())
        {
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
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight));

        engine.addSystem(new PhysixRenderSystem(entityManager, physixManager));
        engine.addSystem(new PhysixUpdateSystem(entityManager, physixManager));
        engine.addSystem(new TestRenderSystem(entityManager));
        engine.addSystem(new TestInputUpdateSystem(entityManager));
        engine.addSystem(new ApplyPlayerInputSystem(entityManager));

        texture = assetManager.getTexture("logo");
    }

    public TiledMap loadMap(String filename)
    {
        try
        {
            return new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex)
        {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }
    }

    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight)
    {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(x, y)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(width, height));
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (button == 0)
        {
            addBall(screenX, screenY);
        }
        return true;
    }

    public void addBall(int x, int y)
    {
        int entity = entityManager.createEntity();
        BallPhysicsComponent ballPhysicsComponent = new BallPhysicsComponent(x, y, 30.0f);
        ballPhysicsComponent.initPhysics(physixManager);

        TestInputComponent testInputComponent = new TestInputComponent();
        entityManager.addComponent(entity, ballPhysicsComponent);
        entityManager.addComponent(entity, testInputComponent);
    }

    @Override
    public void render()
    {
        for (Layer layer : map.getLayers())
        {
            mapRenderer.render(0, 0, layer);
        }
        engine.render();
    }

    @Override
    public void update(float delta)
    {
        engine.update(delta);
    }
}
