package de.hochschuletrier.gdw.ss14.sandbox.maploadingtest;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.physics.box2d.*;
import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.resourcelocator.*;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.commons.tiled.tmx.*;
import de.hochschuletrier.gdw.commons.tiled.utils.*;
import de.hochschuletrier.gdw.commons.utils.*;
import de.hochschuletrier.gdw.ss14.sandbox.Test.Entity.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.*;

import java.util.*;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class MapManager
{
    private EntityManager entityManager;
    private PhysixManager physixManager;

    private TiledMap tiledMap;
    private String folderLocation = "data/maps/";
    private String fileType = ".tmx";

    HashMap<TileSet, Texture> tilesetImages;

    private EntityFactory entityFactory;

    public MapManager(EntityManager entityManager, PhysixManager physixManager, AssetManagerX assetmanager)
    {
        this.entityManager = entityManager;
        this.physixManager = physixManager;
        this.entityFactory = new EntityFactory(entityManager, physixManager, assetmanager);
        tilesetImages = new HashMap();
    }

    public void loadMap(String mapName)
    {
        String filename = folderLocation + mapName + fileType;
        try
        {
            this.tiledMap = new TiledMap(filename, LayerObject.PolyMode.ABSOLUTE);
        } catch (Exception ex)
        {
            throw new IllegalArgumentException(
                    "Map konnte nicht geladen werden: " + filename);
        }

        createTileSet();
        createPhysics();
        loadMapObjects();
    }

    public HashMap getTileSet()
    {
        return tilesetImages;
    }

    public void createTileSet()
    {
        for (TileSet tileset : tiledMap.getTileSets())
        {
            TmxImage img = tileset.getImage();
            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
            tilesetImages.put(tileset, new Texture(filename));
        }
    }

    private void createPhysics()
    {
        // Generate static world
        int tileWidth = tiledMap.getTileWidth();
        int tileHeight = tiledMap.getTileHeight();
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight));
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

    public TiledMap getMap()
    {
        return this.tiledMap;
    }

    private void loadMapObjects()
    {
        ArrayList<Layer> layers = this.tiledMap.getLayers();

        for (int i = 0; i < layers.size(); ++i)
        {
            ArrayList<LayerObject> mapObjects = layers.get(i).getObjects();

            if (mapObjects != null)
            {
                for (int j = 0; j < mapObjects.size(); ++j)
                {
                    String objType = mapObjects.get(j).getName();

                    switch (objType)
                    {
                        case "start":
                            // TODO: add object with entityFactory here
                            break;
                        case "wool":
                            // TODO: add object with entityFactory here
                            break;

                        case "vase":
                            // TODO: add object with entityFactory here
                            break;

                        case "broom":
                            // TODO: add object with entityFactory here
                            break;

                        case "lamp":
                            // TODO: add object with entityFactory here
                            break;

                        case "food":
                            // TODO: add object with entityFactory here
                            break;

                        case "door":
                            // TODO: add object with entityFactory here
                            break;

                        case "catbox":
                            // TODO: add object with entityFactory here
                            break;

                        case "stairs":
                            // TODO: add object with entityFactory here
                            break;
                    }
                }   // end for (mapObjects)
            }
        }   // end for (layer)
    }

}
