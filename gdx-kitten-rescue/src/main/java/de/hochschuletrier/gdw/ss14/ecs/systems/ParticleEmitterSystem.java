package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBody;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixManager;
import de.hochschuletrier.gdw.ss14.ecs.Engine;
import de.hochschuletrier.gdw.ss14.ecs.EntityFactory;
import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.CirclePhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.LimitedLifetimeComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.states.ParticleEmitterTypeEnum;

/**
 * 
 * @author Milan Rüll
 *
 */
public class ParticleEmitterSystem extends ECSystem {
    
    private TextureRegion[] LiquidParticleTextures;
    private TextureRegion[] PawParticleTextures;

    public ParticleEmitterSystem(EntityManager entityManager) {
        
        super(entityManager);
        loadParticleTextures();
    }

    @Override
    public void update(float delta) {
        
        Array<Integer> arr = entityManager.getAllEntitiesWithComponents(ParticleEmitterComponent.class, PhysicsComponent.class);
        
        for (Integer ent : arr) {
            
            ParticleEmitterComponent emitComp = entityManager.getComponent(ent, ParticleEmitterComponent.class);
            
            // Color fading
            /*if (emitComp.fadeColor != null) {
                
                emitComp.fadingTimeLeft -= delta;
                
                // TODO: continue coding here =D
            }*/
            
            int lastCycle = (int)(emitComp.lifetimeLeft / emitComp.emitInterval);
            emitComp.lifetimeLeft -= delta;
            int currentCycle = (int)(emitComp.lifetimeLeft / emitComp.emitInterval);
            
            // Kill emitter if lifetime has expired
            if ((emitComp.hasLimitedLifetime) && (emitComp.lifetimeLeft < 0.0f)) {
                
                entityManager.removeComponent(ent, emitComp);
            }
            
            // Emit one particle for every passed cycle since last update
            for (int i=lastCycle-currentCycle-1; i >= 0 ; --i) {
                        
                emitParticle(emitComp, entityManager.getComponent(ent, PhysicsComponent.class), currentCycle-i);
                
                // Add one interval to lifetime if emitter has unlimited lifetime so no overflow is possible
                /*if (!emitComp.hasLimitedLifetime)
                    emitComp.lifetimeLeft += emitComp.emitInterval;*/
            }
        }
    }
    
    private void emitParticle( ParticleEmitterComponent emitComp, PhysicsComponent physComp, int cycleNumber ) {
        
        Vector2 particlePos = new Vector2();
        TextureRegion tex = null;
        float rotation = 0.0f;
        
        switch (emitComp.emitterType) {
        
        case LiquidParticleEmitter:
            int index = (int) (Math.random()*LiquidParticleTextures.length);
            tex = LiquidParticleTextures[index];
            rotation = (float)(Math.random()*360.0);
            
            // Get random direction and then a random position within the emit radius
            particlePos = new Vector2(1f, 0f).rotate((float)(Math.random()*360.0));
            particlePos.scl((float)(Math.random() * emitComp.emitRadius));
            particlePos.add(physComp.getPosition());

            break;
            
        case PawParticleEmitter:
            tex = PawParticleTextures[Math.abs(cycleNumber) % PawParticleTextures.length];
            rotation = physComp.getRotation();
            
            // Alternate between right and left
            float dir = (cycleNumber % 2 == 0) ? -1f : 1f;
            particlePos = new Vector2(emitComp.emitRadius*dir, 0.0f).rotate(physComp.getRotation());
            particlePos.add(physComp.getPosition());
            
            break;
        }       
            
        createParticle(particlePos, rotation, emitComp.particleTintColor, emitComp.particleLifetime, tex);
    }
    
    private void createParticle( Vector2 emitPosition, float rotation, Color tintColor, float lifetime, TextureRegion tex ) {
        
        int particleEnt = entityManager.createEntity();
        Vector2 particlePos = emitPosition;
        
        PhysicsComponent particlePhysics = new PhysicsComponent();
        particlePhysics.defaultPosition = particlePos.cpy();
        particlePhysics.defaultRotation = rotation;
        
        RenderComponent particleRender = new RenderComponent();
        particleRender.zIndex = -1;
        particleRender.texture = tex;
        particleRender.tintColor = tintColor.cpy();
        
        LimitedLifetimeComponent limitComp = new LimitedLifetimeComponent();
        limitComp.lifetimeLeft = lifetime;
        limitComp.graduallyReduceAlpha = true;
        
        entityManager.addComponent(particleEnt, particlePhysics);
        entityManager.addComponent(particleEnt, particleRender);
        entityManager.addComponent(particleEnt, limitComp);
    }
    
    private void loadParticleTextures() {
        
        Texture tex;
                
        tex = new Texture("data/animations/PartikelBlut.png");
        LiquidParticleTextures = TextureRegion.split(tex, tex.getWidth() / 10, tex.getHeight())[0];
        
        tex = new Texture("data/animations/Pfoten.png");
        PawParticleTextures = TextureRegion.split(tex, tex.getWidth() / 2, tex.getHeight())[0];
    }
    
    @Override
    public void render() {
        // Nothing to do
    }     
}
