package de.hochschuletrier.gdw.ss14.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PlayerComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.TileMapRenderingComponent;
import de.hochschuletrier.gdw.ss14.states.GroundTypeState;

/**
 * Created by Daniel Dreher on 01.10.2014.
 */
public class MapManager
{
    private enum tile2entity{
        none, waterpuddle, deadzone, bloodpuddle, parketfloor, kitchenfloor, passable
    }
    private EntityManager entityManager;

    private PhysixManager physixManager;
    private TiledMap tiledMap;
    private String folderLocation = "data/maps/";

    private String fileType = ".tmx";

    HashMap<TileSet, Texture> tilesetImages;
    private int levelEntity;

    public int currentFloor = -1;
    public int targetFloor = 0;


    public boolean isChangingFloor = false;

    public MapManager(EntityManager entityManager, PhysixManager physixManager, AssetManagerX assetmanager)
    {
        this.entityManager = entityManager;
        this.physixManager = physixManager;
        tilesetImages = new HashMap();

        levelEntity = entityManager.createEntity();
    }

    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, short floor, boolean flag, String userdata)
    {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBody body = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(x, y)
                .fixedRotation(false).create();
        body.createFixture(new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(width, height)
                .category(floor));
        body.getFixtureList().forEach((f)->f.setUserData(userdata));
        body.getFixtureList().forEach((f)->f.setSensor(flag));

    }

    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, short floor, String userdata){
        addShape(physixManager, rect, tileWidth, tileHeight, floor, false, userdata);
    }

    private void addShape(PhysixManager physixManager, Rectangle rect, int tileWidth, int tileHeight, short floor, tile2entity t2e)
    {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        PhysixBodyDef bodydef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixManager).position(x, y)
                .fixedRotation(false);

        PhysixFixtureDef fixturedef = new PhysixFixtureDef(physixManager).density(1).friction(0.5f).shapeBox(width, height)
                .category(floor).mask((short) 0b1111111111111111);
        switch (t2e) {
        case none:
            PhysixBody body = bodydef.create();
            body.createFixture(fixturedef);
            body.getBody().setUserData("wall");
            break;
        case waterpuddle: EntityFactory.constructPuddleOfWater(bodydef, fixturedef);break;
        case bloodpuddle:
            fixturedef.sensor(true);
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
//        case passable:
//            PhysixBody physixBody = bodydef.create();
//            physixBody.createFixture(fixturedef);
//            physixBody.getBody().setUserData("wall");
//            // TODO: set category and mask
//            break;

        }

    }

    private void createPhysics()
    {
        // Generate static world
        int tileWidth = tiledMap.getTileWidth();
        int tileHeight = tiledMap.getTileHeight();
        //


        RectangleGenerator generator = new RectangleGenerator();
        for(int i = 0; i < 16; i++){
            short floor = (short)(Math.pow(2, i));
            final int j = i;
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) ->info.getBooleanProperty("blocked", false)
                   && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, "wall"));
        generator.generate(tiledMap,
                    (Layer layer, TileInfo info) ->info.getBooleanProperty("passable", false)
                            && (layer.getIntProperty("floor", 0) == j) ,
                    (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.passable));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("deadzone", false)
                                   && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.deadzone));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("blood puddle", false)
                && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.bloodpuddle));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("water puddle", false)
                && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.waterpuddle));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("woodfloor", false)
                && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.parketfloor));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("woodbeam", false)
                && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.parketfloor));
        generator.generate(tiledMap,
                (Layer layer, TileInfo info) -> info.getBooleanProperty("kitchenfloor", false)
                && (layer.getIntProperty("floor", 0) == j) ,
                (Rectangle rect) -> addShape(physixManager, rect, tileWidth, tileHeight, floor, tile2entity.kitchenfloor));

        }
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

    public TiledMap getMap()
    {
        return this.tiledMap;
    }

    public HashMap getTileSet()
    {
        return tilesetImages;
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

    public void loadMap(String path)
    {
        String filename = path;
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

                short mask = 0, category = 0;
                for (int j = 0; j < mapObjects.size(); ++j)
                {

                    String objType = mapObjects.get(j).getName();
                    float x = mapObjects.get(j).getX();
                    float y = mapObjects.get(j).getY();
                    Vector2 pos = new Vector2(x, y);
                    float width = mapObjects.get(j).getWidth();
                    float height = mapObjects.get(j).getHeight();

                    mask = (short)Math.pow(2, layers.get(i).getIntProperty("floor", 0));
                    category = (short)0b1111111111111111;

                    if (objType != null)
                    {
                        switch (objType)
                        {
                            case "start":
                                if(entityManager.getAllEntitiesWithComponents(PlayerComponent.class).size == 0)
                                {
                                    EntityFactory.constructCat(pos, 150, 75, 0, 50.0f, mask, category );
                                }
                                break;

                            case "dogspawn":
                                /* dogs are build later */
                                dogpositions.add(pos);
                                dogIds.add(mapObjects.get(j).getIntProperty("ID", -1));
                                dogpatrolstring.add(mapObjects.get(j).getProperty("pat", ""));
                                break;
                            case "dogpat":
                                /* used for building dogs */
                                patrolspots.add(pos);
                                spotIds.add(mapObjects.get(j).getIntProperty("ID", -1));
                                break;
                            case "wool":
                                EntityFactory.constructWool(pos, mask, category);
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

                            case "finish":
                                // TODO: add object with entityFactory here
                                EntityFactory.constructFinish(pos, width, height);
//                                EntityFactory.constructMapChangeObj(pos, width, height, nextMap);
                                break;
// isn't an object
//                            case "door":category
//                                // TODO: add object with entityFactory here
//                                break;

                            case "catbox":
                                EntityFactory.constructCatbox(pos, mask, category);
                                break;

                            case "stairs":
                                // read out floor of mapobject properties, default is 0.
                                int floor = 1;

                                if(mapObjects.get(j).getProperties().getString("stairs") != null)
                                {
                                    String floorTargetProperty = mapObjects.get(j).getProperties().getString("stairs");
                                    floor = Integer.parseInt(floorTargetProperty);
                                }

                                EntityFactory.constructStairs(pos, width, height, floor, mask, category);
                                break;
                        }
                    }
                }   // end for (mapObjects)

                /* build dogs now */
                for(Vector2 pos : dogpositions){

                    ArrayList<Vector2> tmp = new ArrayList<>();
                    for(String s : dogpatrolstring){
                        if(dogpatrolstring.size() == 0) continue;
                        if(spotIds.size() == 0) continue;
                        for(String r : s.split(",")){
                            if(!r.isEmpty())
                            {
                                if (spotIds.get(Integer.parseInt(r)) <= 0)
                                {
                                    continue;
                                }
                                if (r != "")
                                {
                                    tmp.add(patrolspots.get(spotIds.get(Integer.parseInt(r))));
                                }
                            }
                        }
                    }
                    EntityFactory.constructSmartDog(pos, 100, 85, 0, 30, tmp, mask, category);
                }
                /* end build dogs */
            }
        }   // end for (layer)
    }

    public void resetMap()
    {
        entityManager.deleteAllGameplayRelatedEntitiesExcludingCat();
        setFloor(Game.mapManager.currentFloor);
    }

    public void setFloor(int floor)
    {

        currentFloor = floor;

        TileMapRenderingComponent mapComp = entityManager.getComponent(levelEntity, TileMapRenderingComponent.class);
        Array<Integer> newRenderedLayers = new Array<Integer>();

        for (Layer layer : mapComp.getMap().getLayers())
        {
            //System.out.println(this.getClass().getName()+": "+layer.getIntProperty("floor", -1));
            if ((layer.getIntProperty("floor", -1) == floor) && (layer.isTileLayer())) {

                newRenderedLayers.add(mapComp.getMap().getLayers().indexOf(layer));
            }
        }

        mapComp.fadeToRenderedLayers(newRenderedLayers);
        loadMapObjects();
    }
}
