package de.hochschuletrier.gdw.ss14.sandbox.ecs.systems;

import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.sandbox.ecs.*;

/**
 * Created by Dani on 30.09.2014.
 */

public class PhysixUpdateSystem extends ECSystem
{
    public static final int POSITION_ITERATIONS = 3;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final float STEP_SIZE = 1 / 30.0f;

    private PhysixManager physixManager;

    public PhysixUpdateSystem(EntityManager entityManager, PhysixManager physixManager)
    {
        super(entityManager);
        this.physixManager = physixManager;
    }

    @Override
    public void update(float delta)
    {
        physixManager.update(STEP_SIZE, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    @Override
    public void render()
    {

    }
}
