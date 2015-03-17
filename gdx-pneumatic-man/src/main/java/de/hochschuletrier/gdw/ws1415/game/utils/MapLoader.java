package de.hochschuletrier.gdw.ws1415.game.utils;

import java.util.ArrayList;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
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
                          System.out.print( tiles[i][j].localId );
                      }
                  }
                  System.out.println();
              }
          }
          
          
        } catch(NullPointerException e)
        {
            throw new IllegalArgumentException( "Tileset ist Null" );
        }
        catch(Exception e)
        {
          throw new IllegalArgumentException( "Map konnte nicht geladen werden: " + filename );
        }
    }
          
    // wenn moeglich nicht benutzen
    public TiledMap getTiledMap()
    {
      return tiledMap;
    }
    
    public ArrayList<Layer> getLayers()
    {
      return tiledMap.getLayers();
    }
    
    public Layer getLayer( int index )
    {
      return tiledMap.getLayers().get(index);
    }
    
    public ArrayList<Entity> getEntities()
    {
      return entities;
    }
      
    public Entity getEntity( int index)
    {
      return entities.get( index );
    }
          
    public int getRescuingMinersLeft()
    {
        // TODO
        return 0;
    }

    private void loadMapObjects()
    {
        
    }

    /*
    public static void main( String args[])
    {
        MapLoader ml = new MapLoader("data/maps/Test_Physics.tmx");
    
    }*/
}
