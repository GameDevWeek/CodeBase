package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.*;

import de.hochschuletrier.gdw.commons.ai.behaviourtree.engine.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.game.*;

/**
 * Created by Daniel Dreher on 04.10.2014.
 */
public class ChangeMapSystem extends ECSystem {

    public ChangeMapSystem(EntityManager entityManager) {
        super(entityManager, 0);
    }

    @Override
    public void update(float delta) {
        if (Game.mapManager != null) {
            if (Game.mapManager.isChangingFloor) {
                entityManager.deleteAllGameplayRelatedEntitiesExcludingCat();
                Game.mapManager.isChangingFloor = false;
                Game.mapManager.setFloor(Game.mapManager.targetFloor);
                System.out.println("Changed floor to "
                        + Game.mapManager.targetFloor);

                Array<Integer> entities = entityManager
                        .getAllEntitiesWithComponents(PlayerComponent.class,
                                PhysicsComponent.class);

                for (Integer entity : entities) {

                    CatPhysicsComponent cpc = (CatPhysicsComponent) entityManager
                            .getComponent(entity, PhysicsComponent.class);

                    int newCat = EntityFactory.constructCat(cpc.getPosition(), 150, 75, 0,
                            50.0f, (short) (cpc.mask << 1), cpc.category);

                    // set lives
                    CatPropertyComponent catPropertyComponentOld = entityManager.getComponent(entity, CatPropertyComponent.class);
                    CatPropertyComponent catPropertyComponentNew = entityManager.getComponent(newCat, CatPropertyComponent.class);

                    if(catPropertyComponentOld != null && catPropertyComponentNew != null)
                    {
                        catPropertyComponentNew.amountLives = catPropertyComponentOld.amountLives;
                    }

                    entityManager.deletePhysicEntity(entity);
                }
            }
        }

    }

    @Override
    public void render() {

    }
}
