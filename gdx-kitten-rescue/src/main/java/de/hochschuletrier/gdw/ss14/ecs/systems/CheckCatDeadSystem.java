package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.AnimationComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.CatPropertyComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.MovementComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.states.CatStateEnum;

/**
 * Created by Daniel Dreher on 02.10.2014.
 */
public class CheckCatDeadSystem extends ECSystem
{
    private PhysixManager physixManager;

    public CheckCatDeadSystem(EntityManager entityManager, PhysixManager physixManager)
    {
        super(entityManager, 0);
        this.physixManager = physixManager;
    }

    @Override
    public void render()
    {

    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(CatPropertyComponent.class, PhysicsComponent.class, MovementComponent.class);

        for (Integer entity : entities)
        {
            PhysicsComponent physicsComponent = entityManager.getComponent(entity, PhysicsComponent.class);
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
            MovementComponent movementComponent = entityManager.getComponent(entity, MovementComponent.class);
            AnimationComponent animationComponent = entityManager.getComponent(entity, AnimationComponent.class);

            if(catPropertyComponent.getState() == CatStateEnum.FALL || catPropertyComponent.getState() == CatStateEnum.DIE)
            {
                Array<Fixture> fixtures = physicsComponent.physicsBody.getFixtureList();
                for (Fixture fixture : fixtures)
                {
                    fixture.setSensor(true);
                }

                if(animationComponent.isFinished)
                {
                    catPropertyComponent.isAlive = false;
                }
            }

            if (!catPropertyComponent.isAlive)
            {
                // respawn cat at last checkpoint
                Vector2 lastCheckpoint = catPropertyComponent.lastCheckPoint;
                float minVelocity = movementComponent.minVelocity;
                float middleVelocity = movementComponent.middleVelocity;
                float maxVelocity = movementComponent.maxVelocity;
                float acceleration = movementComponent.acceleration;

                int newAmountLives = catPropertyComponent.amountLives - 1;
                if(newAmountLives<0)
                {
                    newAmountLives = 0;
                }

                // destroy entity (can't change position because of box2d)

                short mask = physicsComponent.physicsBody.getFixtureList().get(0).getFilterData().maskBits;
                short category = physicsComponent.physicsBody.getFixtureList().get(0).getFilterData().categoryBits;

                physixManager.destroy(physicsComponent.physicsBody);
                entityManager.deleteEntity(entity);

                // create new player entity
                int newPlayer = EntityFactory.constructCat(lastCheckpoint, maxVelocity, middleVelocity, minVelocity, acceleration, mask, category);

                // set new lives
                entityManager.getComponent(newPlayer, CatPropertyComponent.class).amountLives = newAmountLives;

                Game.mapManager.resetMap();
            }
        }

    }
}
