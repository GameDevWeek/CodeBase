package de.hochschuletrier.gdw.ss14.game;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import de.hochschuletrier.gdw.commons.gdx.assets.*;
import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.commons.resourcelocator.*;
import de.hochschuletrier.gdw.commons.tiled.*;
import de.hochschuletrier.gdw.commons.tiled.tmx.*;
import de.hochschuletrier.gdw.commons.tiled.utils.*;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.ecs.systems.CatContactSystem;

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

    private int levelEntity;

    public MapManager(EntityManager entityManager, PhysixManager physixManager, AssetManagerX assetmanager)
    {
        this.entityManager = entityManager;
        this.physixManager = physixManager;
        tilesetImages = new HashMap();

        levelEntity = entityManager.createEntity();
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

        TileMapRenderingComponent mapComp;
        mapComp = entityManager.getComponent(levelEntity, TileMapRenderingComponent.class);

        if (mapComp == null)
        {
            mapComp = new TileMapRenderingComponent();
            entityManager.addComponent(levelEntity, mapComp);
        }

        mapComp.setMap(getMap());
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

    public void setFloor(int floor)
    {

        TileMapRenderingComponent mapComp = entityManager.getComponent(levelEntity, TileMapRenderingComponent.class);
        mapComp.renderedLayers.clear();

        for (Layer layer : mapComp.getMap().getLayers())
        {
            System.out.println(this.getClass().getName()+": "+layer.getIntProperty("floor", -1));
            //if (layer.getIntProperty("floor", -1) == floor) 
            if (layer.isTileLayer())
                mapComp.renderedLayers.add(mapComp.getMap().getLayers().indexOf(layer));
        }
    }

    private void createPhysics()
    {
        // Generate static world
        int tileWidth = tiledMap.getTileWidth();
        int tileHeight = tiledMap.getTileHeight();
        short floor = (short)Math.pow(2, tiledMap.getIntProperty("floor", 0));
        
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blocked", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, "wall"));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("deadzone", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, true, "deadzone"));
    }

    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, int floor, String userdata){
        addShape(physixManager, rect, tileWidth, tileHeight, floor, false, userdata);
    }
    
    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, int floor){
        addShape(physixManager, rect, tileWidth, tileHeight, floor, false, "");
    }
    
    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, int floor, boolean flag, String userdata)
    {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(x, y)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(width, height)
                .category((short)floor));
        body.getFixtureList().forEach((f)->f.setUserData(userdata));
        body.getFixtureList().forEach((f)->f.setSensor(flag));
        
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

                    if (objType != null)
                    {
                        switch (objType)
                        {
                            case "start":
                                float x = mapObjects.get(j).getX();
                                float y = mapObjects.get(j).getY();
                                Vector2 pos = new Vector2(x, y);
                                try {
                                    CatContactSystem ccs = (CatContactSystem)Game.engine.getSystemOfType(CatContactSystem.class);
                                    EntityFactory.constructCat(pos, 150, 75, 0, 50.0f, ccs);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

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

// isn't an object
//                            case "door":
//                                // TODO: add object with entityFactory here
//                                break;

                            case "catbox":
                                // TODO: add object with entityFactory here
                                break;

                            case "stairs":
                                // TODO: add object with entityFactory here
                                break;
                        }
                    }
                }   // end for (mapObjects)
            }
        }   // end for (layer)
    }
}
