package de.hochschuletrier.gdw.ss14.ecs.components;

import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.ss14.states.ParticleEmitterTypeEnum;

/**
 * @author Milan Rüll
 */
public class ParticleEmitterComponent implements Component {
    
    // The lifetime of the emitter in seconds (if has limited lifetime)
    public boolean hasLimitedLifetime = false;
    public float lifetimeLeft = 2.0f;
    
    public float particleLifetime = 5.0f;
    
    public float emitInterval = 0.1f;
    public float emitRadius = 50f;
    
    /*public boolean isDirectionalEmitter = false;
    public Vector2 emitDirection;
    public float emitAngle;*/
    
    public ParticleEmitterTypeEnum emitterType = ParticleEmitterTypeEnum.LiquidParticleEmitter;
}
