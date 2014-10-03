package de.hochschuletrier.gdw.ss14.game.ecs.systems;

import de.hochschuletrier.gdw.commons.gdx.physix.*;
import de.hochschuletrier.gdw.ss14.game.ecs.*;

/**
 * Created by Dani on 29.09.2014.
 */
public class PhysixRenderSystem extends ECSystem
{
    private PhysixManager physixManager;

    public PhysixRenderSystem(EntityManager entityManager, PhysixManager physixManager)
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
