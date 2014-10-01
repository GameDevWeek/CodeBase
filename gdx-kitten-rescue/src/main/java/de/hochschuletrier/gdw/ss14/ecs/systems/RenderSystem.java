package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;

/**
 * Draws all RenderComponents.
 * @author David Neubauer
 */
public class RenderSystem extends ECSystem {

    private final SpriteBatch batch;

    public RenderSystem(EntityManager entityManager, int priority) {
        super(entityManager, priority);
        batch = new SpriteBatch();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        Array<Integer> entites = entityManager.getAllEntitiesWithComponents(RenderComponent.class, PhysicsComponent.class);

        RenderComponent renderCompo;
        PhysicsComponent physicsCompo;
        
        batch.begin();
        for (Integer integer : entites) {
            renderCompo = entityManager.getComponent(integer, RenderComponent.class);
            physicsCompo = entityManager.getComponent(integer, PhysicsComponent.class);
            
           batch.draw(renderCompo.texture, physicsCompo.dummyPosition.x, physicsCompo.dummyPosition.y);
        }
        batch.end();
    }

}
