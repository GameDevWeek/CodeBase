package de.hochschuletrier.gdw.ss14.ecs.systems;

import org.lwjgl.util.vector.Matrix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil.Mode;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CameraComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LightComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

public class LightMapSystem extends ECSystem{
	TextureRegion shadow;

	public LightMapSystem(EntityManager entityManager) {
		super(entityManager);
		shadow = new TextureRegion(new Texture("data/images/shadow.png"));
	}
	
	public LightMapSystem(EntityManager entityManager, int priority) {
		super(entityManager, priority);
		shadow = new TextureRegion(new Texture("data/images/shadow.png"));
	}

	@Override
	public void update(float delta) {		
	}

	@Override
	public void render() {
		Array<Integer> entities = entityManager.getAllEntitiesWithComponents(RenderComponent.class, LightComponent.class);
		
		for(Integer currentEnt : entities){
			
			LightComponent lightComp = entityManager.getComponent(currentEnt, LightComponent.class);
			RenderComponent renderComp = entityManager.getComponent(currentEnt, RenderComponent.class);
			PhysicsComponent pComp = entityManager.getComponent(currentEnt, PhysicsComponent.class);
			// if shadow isn't not existin.... :Ds
			if(lightComp.alpha > 0.0f){
				
				// set up all the alpha stuff
				DrawUtil.batch.end();
				Gdx.gl.glEnable(GL20.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				DrawUtil.setColor(new Color(1,1,1,lightComp.alpha));
				DrawUtil.batch.begin();
				
				
				float lightWidth = Gdx.graphics.getWidth() ;
				float lightHeight = Gdx.graphics.getHeight();
				
				// check if camera is on entity with light component. if yes, 
				CameraComponent cameraComp = entityManager.getComponent(currentEnt, CameraComponent.class);
				if(cameraComp!=null){
					lightWidth += cameraComp.maxScreenCenterDistance * 2;
					lightHeight += cameraComp.maxScreenCenterDistance * 2;
				}
				
				DrawUtil.batch.draw(shadow,
						pComp.getPosition().x - (lightWidth / 2), 
						pComp.getPosition().y - (lightHeight / 2), 
                        lightWidth / 2, 
                        lightHeight / 2, 
                        lightWidth, 
                        lightHeight, 
                        1f, 
                        1f, 
                        0);
			
				
				DrawUtil.batch.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				DrawUtil.resetColor();
				DrawUtil.batch.begin();
			}
		}
	}
}
