package de.hochschuletrier.gdw.ss14.sandbox.Test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss14.sandbox.SandboxGame;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.Engine;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.EntityManager;

import java.util.HashMap;

public class MapVersuch extends SandboxGame{

    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private Engine engine;
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private TiledMap map;
    private TiledMapRendererGdx mapRenderer;


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
    public void dispose() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        engine = new Engine();
        entityManager = new EntityManager();
        physixManager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);

        map = loadMap("data/maps/SebFirstPlayable_reworked.tmx");
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

    @Override
    public void render() {
        engine.render();
        mapRenderer.render(0, 0);
    }

    @Override
    public void update(float delta) {
        engine.update(delta);
    }
}
