package de.hochschuletrier.gdw.ss14.ecs.systems;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.ShadowComponent;

/**
 * Draws dark Ellipse under Entities with ShadowComponent
 * 
 * @author David Liebemann
 *
 */
public class ShadowSystem extends ECSystem{
	
	private TextureRegion shadow = null;
	

	public ShadowSystem(EntityManager entityManager) {
		super(entityManager);
		 shadow = new TextureRegion(new Texture("data/images/shadow.png"));
	}
	
	public ShadowSystem(EntityManager entityManager, int priority) {
		super(entityManager, priority);
		shadow = new TextureRegion(new Texture("data/images/shadow.png"));
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render() {
		Array<Integer> entities = entityManager.getAllEntitiesWithComponents(RenderComponent.class, PhysicsComponent.class, ShadowComponent.class);
		
		for(Integer currentEnt : entities){
			RenderComponent renderComp = entityManager.getComponent(currentEnt, RenderComponent.class);
			PhysicsComponent physicsComp = entityManager.getComponent(currentEnt, PhysicsComponent.class);
			ShadowComponent shadowComp = entityManager.getComponent(currentEnt, ShadowComponent.class);
			
			if(shadowComp.alpha > 0f){
				// set alpha
				DrawUtil.batch.end();
				DrawUtil.batch.setColor(0f, 0f, 0f, shadowComp.alpha);
				DrawUtil.batch.begin();
				
				float shadowWidth = renderComp.texture.getRegionWidth() * shadowComp.z;
				float shadowHeight = renderComp.texture.getRegionHeight() * shadowComp.z;
				
				DrawUtil.batch.draw(shadow,
                        physicsComp.getPosition().x - (shadowWidth / 2), 
                        physicsComp.getPosition().y - (shadowHeight / 2), 
                        shadowWidth / 2, 
                        shadowHeight / 2, 
                        shadowWidth, 
                        shadowHeight, 
                        1f, 
                        1f, 
                        (float)(physicsComp.getRotation() * 180 / Math.PI));
				
			}
			
			
		}
		
	}
	

}
