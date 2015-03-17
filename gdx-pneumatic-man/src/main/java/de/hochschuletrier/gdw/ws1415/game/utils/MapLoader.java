package de.hochschuletrier.gdw.ws1415.game.utils;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.SafeProperties;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;


// 
public class MapLoader {
    
    private final static int TILE_NO_ENTITY = 0;
    // Test Entity
    private final static int TILE_ENTITY_X = 1;
  
  
    private TiledMap tiledMap;
    private ArrayList<Entity> entities;
  
    public MapLoader( String filename )
    {
        // versuche map zu laden,
        // wenn nicht geladen werden kann wird
        // throw IllegalArgument
        try
        {
            tiledMap = new TiledMap( filename, LayerObject.PolyMode.ABSOLUTE );

          
          // laufe ueber alle Layer und suche nach Entities
          for( Layer l : tiledMap.getLayers() )
          {
              if ( l == null ) continue;
              TileInfo[][] tiles =  l.getTiles();
              
              /*
              if ( tiles != null )
              for(int i=0;i<tiles.length;i++)
              {
                  if ( tiles[i] != null )
                  for(int j=0;j<tiles[i].length;j++)
                  {
                      if ( tiles[i][j] != null )
                      {
                          switch( tiles[i][j].localId )
                          {
                          case TILE_NO_ENTITY:
                            break;
                          case TILE_ENTITY_X:
                            break;
                          default:
                        
                          break;
                          }
                      }
                  }
              }
              */
          }
          
          
        } 
        catch(Exception e)
        {
          throw new IllegalArgumentException( "Map konnte nicht geladen werden: " + filename );
        }
    }
          
    /**
     *  wenn moeglich nicht benutzen
     * @return
     */
    public TiledMap getTiledMap()
    {
      return tiledMap;
    }
    
    /**
     * nicht benutzen
     * @return
     */
    public ArrayList<Layer> getLayers()
    {
      return tiledMap.getLayers();
    }
    
    /**
     * nicht benutzen
     * @param index
     * @return
     */
    public Layer getLayer( int index )
    {
      return tiledMap.getLayers().get(index);
    }
    
    /**
     * 
     * @param layer Layer in dem sich der Blcok befindet
     * @param x X-Koordinate des Blocks
     * @param y Y-Koodinate des Blocks
     * @return Infos über den Block (  )
     */
    public TileInfo getTileInfo(int layer,int x,int y)
    {
        return tiledMap.getLayers().get( layer ).getTiles()[x][y];
    }
    
    /**
     * not working
     * @param layer
     * @param x
     * @param y
     * @return
     */
    
    /*
    public String getBlockTypeOfPosition(int layer,int x,int y)
    {
        // Versuche auf gewuenschten Block zuzugreifen
        try
        {
            // den Layer erhalten
            Layer l = tiledMap.getLayers().get( layer );
            int gTileID;
            // Block auf dem Layer erfragen
            // wenn null ist dort "None"
            if (  l.getTiles()[x][y] == null )
            {
                // globale Tile ID erhalten
                gTileID = l.getTiles()[x][y].globalId;
                // verwendetes  TileSet abfragen 
                TileSet ts = tiledMap.findTileSet( 0 );
                
                //SafeProperties tsp = ts.getTileProperties( gTileID ); 
                
                
            } else
            {
                return "None";
            }
        } 
        catch( ArrayIndexOutOfBoundsException e )
        {
            throw new ArrayIndexOutOfBoundsException("Der gewuenschte Block ist nicht innerhalb der Map");
        }
    }
    */
    
    /** not ready
     * 
     * @return
     */
    public ArrayList<Entity> getEntities()
    {
      return entities;
    }
    
    /** not ready
     * 
     * @return
     */
    public Entity getEntity( int index)
    {
      return entities.get( index );
    }

    /** not ready
     * 
     * @return
     */
    public int getRescuingMinersLeft()
    {
        // TODO
        return 0;
    }

    /*
    public static void main( String args[])
    {
        MapLoader ml = new MapLoader("data/maps/Test_Physics.tmx");
    
    }*/
}
