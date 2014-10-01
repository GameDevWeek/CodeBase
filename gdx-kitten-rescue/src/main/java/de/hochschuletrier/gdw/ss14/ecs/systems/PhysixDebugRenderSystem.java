package de.hochschuletrier.gdw.ss14.ecs.systems;

import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.ecs.*;

/**
 * Created by Daniel Dreher on 30.09.2014.
 */
public class PhysixDebugRenderSystem extends ECSystem
{
    private PhysixManager physixManager;

    public PhysixDebugRenderSystem(EntityManager entityManager, PhysixManager physixManager)
    {
        super(entityManager);
        this.physixManager = physixManager;
    }

    @Override
    public void update(float delta)
    {
    }

    @Override
    public void render()
    {
        physixManager.render();
    }
}
