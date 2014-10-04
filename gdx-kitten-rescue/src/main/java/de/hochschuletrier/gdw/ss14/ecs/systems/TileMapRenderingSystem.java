package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import de.hochschuletrier.gdw.commons.gdx.tiled.TiledMapRendererGdx;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.TileMapRenderingComponent;

import java.util.HashMap;
import java.util.List;

/**
 * Map Rendering System, using TiledMapRendererGdx by Santo Pfingsten
 * 
 * @author David Liebemann
 *
 */
public class TileMapRenderingSystem extends ECSystem{
	
	/**
	 * renderer, doing background drawing stuff
	 */
	private TiledMapRendererGdx renderer;
    private String layerNameToRender;
    private String layerNameNotToRender;
        
    private ShaderProgram mapEffectShader;
    
    public static float mapEffectTimeLeft = 1.0f;  
    private float mapEffectFactor = 0.0f;

	
    public TileMapRenderingSystem(EntityManager entityManager){
        super(entityManager);
        initializeShaders();
    }
    
    public TileMapRenderingSystem(EntityManager entityManager, int priority){
        super(entityManager, priority);
        initializeShaders();
    }
    
    public void setLayerNameToRender(String name) {
        this.layerNameToRender = name;
    }
    public void setLayerNameNotToRender(String name) {
        this.layerNameNotToRender = name;
    }
    public String getLayerNameToRender() {
        return this.layerNameToRender;
    }
    public String getlayerNameToRender() {
        return this.layerNameNotToRender;
    }
    
	
	/**
	 * Render all Layers in all TileMapRenderingComponents
	 * 
	 * Meaning: Every Level will have TileMapRenderingComponents. 
	 * They contain the map data and the Layers that should be drawn at the moment.
	 * These will be rendered.
	 */
	public void render(){
		// Get all TileMapRenderingComponents. Most likely will be only one...
		List<TileMapRenderingComponent> arr = entityManager.getAllComponentsOfType(TileMapRenderingComponent.class);
		
		for(TileMapRenderingComponent currentComp : arr){
			
		    renderer = currentComp.renderer;
		    
			if (renderer == null) {
			    
    			HashMap<TileSet, Texture> tilesetImages = new HashMap<TileSet, Texture>();
    			
    	        for (TileSet tileset : currentComp.getMap().getTileSets()) {
    	            TmxImage img = tileset.getImage();
    	            String filename = CurrentResourceLocator.combinePaths(tileset.getFilename(), img.getSource());
    	            tilesetImages.put(tileset, new Texture(filename));
    	        }
    	        	        
    			renderer = new TiledMapRendererGdx(currentComp.getMap(), tilesetImages);
    			currentComp.renderer = renderer;
			}
			
			setMapEffectShader();
	         			
			// go through the layers that should be rendered			
			for(Integer layerIndex : currentComp.renderedLayers){
				
			    if (currentComp.getMap().getLayers().size() > layerIndex) {
			        
    				Layer layerToRender = currentComp.getMap().getLayers().get(layerIndex);
                                if (layerToRender.isTileLayer()
                                        && (this.layerNameToRender == null || layerToRender.getName().equals(layerNameToRender))
                                        && (this.layerNameNotToRender == null || !layerToRender.getName().equals(layerNameNotToRender))) {
                                    
                                    renderer.render(0, 0, layerToRender);
                                }
			    }
			 }
			
             DrawUtil.batch.setShader(null);
		}
	}
	
	private void setMapEffectShader() {
	    
        DrawUtil.batch.flush();

        DrawUtil.batch.setShader(mapEffectShader);
        mapEffectShader.setUniformf("u_effectFactor", mapEffectFactor);
	}
	
	private void initializeShaders() {
	    
        FileHandle vertShader = Gdx.files.internal("data/shaders/passThrough.vs");
        FileHandle fragShader = Gdx.files.internal("data/shaders/mapEffect.fs");
        mapEffectShader = new ShaderProgram(vertShader, fragShader);
        
        System.out.println(mapEffectShader.getLog());
	}
	
	@Override
	public void shutdown() {
        List<TileMapRenderingComponent> arr = entityManager.getAllComponentsOfType(TileMapRenderingComponent.class);
	    
        for (TileMapRenderingComponent comp : arr) {
            comp.setMap(null);
        }
	}
	
	
	@Override
	public void update(float delta) {
		
	}
}
