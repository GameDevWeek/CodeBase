package de.hochschuletrier.gdw.commons.gdx.physix.systems;

import com.badlogic.ashley.core.Entity;

/**
 * A physics system with a fixed time step. Online tutorials will tell you to use a fixed step to avoid problems.
 * In my test cases, it always caused stuttering, so I made this optional. Use if you encounter simulation problems with PhysixSystem.
 * You can reduce the stuttering by using a higher framerate for the physics than the rendering.
 * For example, if you render at 60 FPS, use a framerate of 120 or even 240 for physics.
 *
 * @author Santo Pfingsten
 */
public class PhysixSystemFixedStep extends PhysixSystem {

    private float timeAccumulator;
    private final float timeStep;
    private float timeProcessedThisFrame;

    public PhysixSystemFixedStep(float scale, int velocityIterations, int positionIterations, float timeStep) {
        super(scale, velocityIterations, positionIterations);
        this.timeStep = timeStep;
    }

    public PhysixSystemFixedStep(float scale, int velocityIterations, int positionIterations, float timeStep, int priority) {
        super(scale, velocityIterations, positionIterations, priority);
        this.timeStep = timeStep;
    }

    public float getTimeAccumulator() {
        return timeAccumulator;
    }

    public float getTimeProcessedThisFrame() {
        return timeProcessedThisFrame;
    }

    @Override
    public void update(float deltaTime) {
        timeProcessedThisFrame = 0;
        timeAccumulator += deltaTime;
        while (timeAccumulator >= timeStep) {
            timeAccumulator -= timeStep;
            world.step(timeStep, velocityIterations, positionIterations);
            world.clearForces();
            timeProcessedThisFrame += timeStep;
        }

        if (timeProcessedThisFrame > 0) {
            for (Entity entity: getEntities()) {
                processEntity(entity, deltaTime);
            }
        }
    }
}
