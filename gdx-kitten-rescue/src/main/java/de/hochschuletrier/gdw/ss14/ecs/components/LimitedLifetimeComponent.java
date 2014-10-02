package de.hochschuletrier.gdw.ss14.ecs.components;

/**
 * 
 * @author Milan Rüll
 * Use this component to limit the lifetime of an entity
 */
public class LimitedLifetimeComponent implements Component {

    public float maxLifetime = 5.0f;
    public boolean graduallyReduceAlpha = false;

    public float currentLifetime = 0.0f;
}
