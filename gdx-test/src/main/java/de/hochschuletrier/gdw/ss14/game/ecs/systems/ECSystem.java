package de.hochschuletrier.gdw.ss14.game.ecs.systems;

import de.hochschuletrier.gdw.ss14.game.ecs.*;

/**
 * Created by Dani on 29.09.2014.
 */
public abstract class ECSystem
{
    private int priority = 5; // lower values will be processed earlier
    protected EntityManager entityManager;

    public ECSystem(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public ECSystem(EntityManager entityManager, int priority)
    {
        this.priority = priority;
        this.entityManager = entityManager;
    }

    public abstract void update(float delta);

    public abstract void render();

    public int getPriority()
    {
        return priority;
    }
}
