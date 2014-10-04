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
import de.hochschuletrier.gdw.ss14.states.GroundTypeState;

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
    private int currentFloor = -1;
    

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
                    "Map konnte nicht geladen werden: " + filename, ex);
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

        currentFloor = floor;

        TileMapRenderingComponent mapComp = entityManager.getComponent(levelEntity, TileMapRenderingComponent.class);
        mapComp.renderedLayers.clear();

        for (Layer layer : mapComp.getMap().getLayers())
        {
            //System.out.println(this.getClass().getName()+": "+layer.getIntProperty("floor", -1));
            if ((layer.getIntProperty("floor", -1) == floor) && (layer.isTileLayer()))
                mapComp.renderedLayers.add(mapComp.getMap().getLayers().indexOf(layer));
        }
    }

    private enum tile2entity{
        none, waterpuddle, deadzone, bloodpuddle, parketfloor, kitchenfloor
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
//        generator.generate(tiledMap,
//                (Layer layer, TileInfo info) -> info.getBooleanProperty("deadzone", false),
//                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, true, "deadzone"));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("deadzone", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.deadzone));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blood puddle", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.bloodpuddle));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("water puddle", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.waterpuddle));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("woodfloor", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.parketfloor));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("woodbeam", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.parketfloor));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("kitchenfloor", false),
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.kitchenfloor));

        
    }

    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, int floor, String userdata){
        addShape(physixManager, rect, tileWidth, tileHeight, floor, false, userdata);
    }
    
    /*
    private PhysixBody getShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, int floor)
    {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(x, y)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(width, height)
                .category((short)floor));
        return body;
    }*/
    
    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, int floor, tile2entity t2e)
    {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBodyDef bodydef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(x, y)
                .fixedRotation(false);
        
        PhysixFixtureDef fixturedef = new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(width, height)
                .category((short)floor); 
        
        switch (t2e) {
        case none: 
            PhysixBody body = bodydef.create();
            body.createFixture(fixturedef);
            break;
        case waterpuddle: EntityFactory.constructPuddleOfWater(bodydef, fixturedef);break;
        case bloodpuddle: 
            fixturedef.sensor(false);
            EntityFactory.constructPuddleOfBlood(bodydef, fixturedef);break;
        case deadzone:
            fixturedef.sensor(true);
            EntityFactory.constructDeadzone(bodydef, fixturedef);break;
        case kitchenfloor:
            fixturedef.sensor(true);
            EntityFactory.constructFloor(bodydef, fixturedef, GroundTypeState.kitchenfloor);break;
        case parketfloor:    
            fixturedef.sensor(true);
            EntityFactory.constructFloor(bodydef, fixturedef, GroundTypeState.woodenfloor);break;
        }
        
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
            if (layers.get(i).getIntProperty("floor", -1) != currentFloor)
                continue;
            
            ArrayList<LayerObject> mapObjects = layers.get(i).getObjects();
            if (mapObjects != null)
            {
                ArrayList<Integer> dogIds = new ArrayList<>();
                ArrayList<Vector2> dogpositions = new ArrayList<>();
                ArrayList<String>  dogpatrolstring = new ArrayList<>();
                
                ArrayList<Vector2> patrolspots = new ArrayList<>();
                ArrayList<Integer> spotIds = new ArrayList<>();
                
                
                for (int j = 0; j < mapObjects.size(); ++j)
                {
                    
                    String objType = mapObjects.get(j).getName();
                    float x = mapObjects.get(j).getX();
                    float y = mapObjects.get(j).getY();
                    Vector2 pos = new Vector2(x, y);
                    float width = mapObjects.get(j).getWidth();
                    float height = mapObjects.get(j).getHeight();

                    if (objType != null)
                    {
                        switch (objType)
                        {
                            case "start":
                                try {
                                    EntityFactory.constructCat(pos, 150, 75, 0, 50.0f);
                                    
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;
                                
                            case "dogspawn":
                                /* bogs are build later */
                                dogpositions.add(pos);
                                dogIds.add(mapObjects.get(j).getIntProperty("ID", -1));
                                dogpatrolstring.add(mapObjects.get(j).getProperty("list", ""));
                                break;
                            case "patrolspot":
                                /* used for building dogs */
                                patrolspots.add(pos);
                                spotIds.add(mapObjects.get(j).getIntProperty("ID", -1));
                                break;
                            case "wool":
                                EntityFactory.constructWool(pos);
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
                                EntityFactory.constructCatbox(pos);
                                break;

                            case "stairs":
                                // read out floor of mapobject properties, default is 0.
                                int floor = 0;

                                if(mapObjects.get(j).getProperties().getString("stairs") != null)
                                {
                                    String floorTargetProperty = mapObjects.get(j).getProperties().getString("stairs");
                                    floor = Integer.parseInt(floorTargetProperty);
                                }

                                EntityFactory.constructStairs(pos, width, height, floor);
                                break;
                        }
                    }
                }   // end for (mapObjects)
                
                /* build dogs now */
                for(Vector2 pos : dogpositions){
                    
                    ArrayList<Vector2> tmp = new ArrayList<>();
                    for(String s : dogpatrolstring){
                        for(String r : s.split(",")){
                            if(!r.isEmpty())
                                tmp.add(patrolspots.get( spotIds.get(Integer.parseInt(r)) ));
                        }
                    }
                    
                    EntityFactory.constructSmartDog(pos, 100, 85, 0, 30, tmp);
                }
                /* end build dogs */
            }
        }   // end for (layer)
    }
}
