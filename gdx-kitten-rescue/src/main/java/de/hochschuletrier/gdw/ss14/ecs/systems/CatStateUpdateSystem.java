package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.utils.*;
import de.hochschuletrier.gdw.ss14.ecs.*;
import de.hochschuletrier.gdw.ss14.ecs.components.*;
import de.hochschuletrier.gdw.ss14.states.*;

/**
 * Created by Daniel Dreher on 03.10.2014.
 */
public class CatStateUpdateSystem extends ECSystem
{
    public CatStateUpdateSystem(EntityManager entityManager)
    {
        super(entityManager);
    }

    @Override
    public void update(float delta)
    {
        Array<Integer> entities = entityManager.getAllEntitiesWithComponents(MovementComponent.class, JumpDataComponent.class, CatPropertyComponent.class);

        for (Integer entity : entities)
        {
            MovementComponent movementComponent = entityManager.getComponent(entity, MovementComponent.class);
            CatPropertyComponent catPropertyComponent = entityManager.getComponent(entity, CatPropertyComponent.class);
            JumpDataComponent jumpDataComponent = entityManager.getComponent(entity, JumpDataComponent.class);

            if(catPropertyComponent.isHidden)
            {
                //catPropertyComponent.setState(CatStateEnum.HIDDEN);
                return;
            }

            if (catPropertyComponent.getState() == CatStateEnum.JUMP)
            {
                if (jumpDataComponent.currentJumpTime >= jumpDataComponent.maxJumpTime)
                {
                    catPropertyComponent.setState(CatStateEnum.IDLE);
                }
            }

            if (catPropertyComponent.getState() != CatStateEnum.JUMP && catPropertyComponent.getState() != CatStateEnum.FALL && catPropertyComponent.getState() != CatStateEnum.DIE)
            {
                if (movementComponent.velocity <= 0.0f)
                {
                    if(catPropertyComponent.playTimeTimer == 0){
                        catPropertyComponent.setState(CatStateEnum.IDLE);
                    }else{
                        catPropertyComponent.setState(CatStateEnum.PLAYS_WITH_WOOL);
                    }
                }
                else if (movementComponent.velocity <= movementComponent.middleVelocity)
                {
                    catPropertyComponent.setState(CatStateEnum.WALK);
                }
                else if (movementComponent.velocity >= movementComponent.middleVelocity)
                {
                    catPropertyComponent.setState(CatStateEnum.RUN);
                }
            }
        }

    }

    @Override
    public void render()
    {

    }
}
