package de.hochschuletrier.gdw.ss14.sandbox.maploadingtest;

import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.gdx.tiled.*;
import de.hochschuletrier.gdw.ss14.sandbox.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.*;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class MapLoadingTest extends SandboxGame
{
    public static final int GRAVITY = 0;
    public static final int BOX2D_SCALE = 40;

    private EntityManager entityManager;
    private Engine engine;
    private PhysixManager physixManager;

    private TiledMapRendererGdx mapRenderer;

    private MapManager mapManager;

    @Override
    public void init(AssetManagerX assetManager)
    {
        engine = new Engine();
        entityManager = new EntityManager();
        physixManager = new PhysixManager(BOX2D_SCALE, 0, GRAVITY);

        mapManager = new MapManager(entityManager, physixManager, assetManager);
        mapManager.loadMap("demo");

        mapRenderer = new TiledMapRendererGdx(mapManager.getMap(), mapManager.getTileSet());
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void render()
    {
        mapRenderer.render(0, 0);
        engine.render();
    }

    @Override
    public void update(float delta)
    {
        engine.update(delta);
    }
}
