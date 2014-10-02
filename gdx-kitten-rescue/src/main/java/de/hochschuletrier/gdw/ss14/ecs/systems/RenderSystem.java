package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

/**
 * Draws all RenderComponents.
 *
 * @author David Neubauer
 */
public class RenderSystem extends ECSystem {

    //private ShaderProgram redTintedShader;

    public RenderSystem(EntityManager entityManager, int priority) {

        super(entityManager, priority);
        initializeShaders();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        Array<Integer> entites = entityManager.getAllEntitiesWithComponents(RenderComponent.class, PhysicsComponent.class);

        RenderComponent renderCompo;
        PhysicsComponent physicsCompo;

        for (Integer integer : entites) {
            renderCompo = entityManager.getComponent(integer, RenderComponent.class);
            physicsCompo = entityManager.getComponent(integer, PhysicsComponent.class);

            if (renderCompo.texture != null) {

                if (renderCompo.tintColor != null) {
                    DrawUtil.batch.end();
                    
                    //Gdx.gl20.glColorMask(true, false, false, true);
                    //DrawUtil.batch.setShader(redTintedShader);
                    DrawUtil.batch.setColor(renderCompo.tintColor);
                    DrawUtil.batch.begin();
                }
                /*else
                    Gdx.gl20.glColorMask(true, true, true, true);*/
                
                DrawUtil.batch.draw(renderCompo.texture,
                        physicsCompo.getPosition().x - (renderCompo.texture.getRegionWidth() /2), 
                        physicsCompo.getPosition().y - (renderCompo.texture.getRegionHeight() / 2), 
                        renderCompo.texture.getRegionWidth() / 2, 
                        renderCompo.texture.getRegionWidth() / 2, 
                        renderCompo.texture.getRegionWidth(), 
                        renderCompo.texture.getRegionHeight(), 
                        1f, 
                        1f, 
                        (float)(physicsCompo.getRotation() * 180 / Math.PI));
                
                if (renderCompo.tintColor != null) {
                    DrawUtil.batch.end();
                    
                    //DrawUtil.batch.setShader(null);
                    DrawUtil.batch.begin();
                }
            }
        }
    }

    private void initializeShaders() {
        /*FileHandle vertShader = Gdx.files.internal("data/shaders/passThrough.vs");
        FileHandle fragShader = Gdx.files.internal("data/shaders/redTinted.fs");
        redTintedShader = new ShaderProgram(vertShader, fragShader);
        
        System.out.println(redTintedShader.getLog());*/
    }
}
