package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.ParticleEmitterComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.PhysicsComponent;
import de.hochschuletrier.gdw.ss14.ecs.components.RenderComponent;
import de.hochschuletrier.gdw.ss14.states.ParticleEmitterTypeEnum;

public class ParticleEmitterSystem extends ECSystem {
    
    private TextureRegion[] LiquidParticleTextures;

    public ParticleEmitterSystem(EntityManager entityManager) {
        
        super(entityManager);
        loadParticleTextures();
    }

    @Override
    public void update(float delta) {
        
        Array<Integer> arr = entityManager.getAllEntitiesWithComponents(ParticleEmitterComponent.class, PhysicsComponent.class);
        
        for (Integer ent : arr) {
            
            ParticleEmitterComponent emitComp = entityManager.getComponent(ent, ParticleEmitterComponent.class);
            
            int lastCycle = (int)(emitComp.lifetimeLeft / emitComp.emitInterval);
            emitComp.lifetimeLeft -= delta;
            int currentCycle = (int)(emitComp.lifetimeLeft / emitComp.emitInterval);
            
            // Kill emitter if lifetime has expired
            if ((emitComp.hasLimitedLifetime) && (emitComp.lifetimeLeft < 0.0f)) {
                
                entityManager.removeComponent(ent, emitComp);
            }
            
            // Emit one particle for every passed cycle since last update
            for (int i=0; i < currentCycle-lastCycle; ++i) {
                                
                emitParticle(emitComp, entityManager.getComponent(ent, PhysicsComponent.class).getPosition());
                
                // Add one interval to lifetime if emitter has unlimited lifetime so no overflow is possible
                if (!emitComp.hasLimitedLifetime)
                    emitComp.lifetimeLeft += emitComp.emitInterval;
            }
        }
    }
    
    private void emitParticle( ParticleEmitterComponent emitComp, Vector2 basePosition ) {
        
        Vector2 particlePos = new Vector2();
        TextureRegion tex = null;
        float rotation = 0.0f;
        
        switch (emitComp.emitterType) {
        
        case LiquidParticleEmitter:
            int index = (int) (Math.random()*LiquidParticleTextures.length);
            tex = LiquidParticleTextures[index];
            break;
        }
        
        // Get random direction and then a random position within the emit radius
        particlePos = new Vector2(1f, 0f).rotate((float)(Math.random()*360.0));
        particlePos.scl((float)(Math.random() * emitComp.emitRadius));
        
        // Get random rotation for the particle texture
        rotation = (float)(Math.random()*360.0);
            
        createParticle(particlePos, rotation, tex);
    }
    
    private void createParticle( Vector2 emitPosition, float rotation, TextureRegion tex ) {
        
        int particleEnt = entityManager.createEntity();
        Vector2 particlePos = emitPosition;
        
        PhysicsComponent particlePhysics = new PhysicsComponent();
        particlePhysics.physicsBody.setPosition(particlePos.cpy());
        particlePhysics.setRotation(rotation);
        
        RenderComponent particleRender = new RenderComponent();
        particleRender.zIndex = -1;
        particleRender.texture = tex;
        
        entityManager.addComponent(particleEnt, particlePhysics);
        entityManager.addComponent(particleEnt, new RenderComponent());
    }
    
    private void loadParticleTextures() {
        
        Texture tex;
                
        tex = new Texture("data/animations/PartikelBlut.png");
        LiquidParticleTextures = TextureRegion.split(tex, tex.getWidth() / 10, tex.getHeight())[0];
    }
    
    @Override
    public void render() {
        // Nothing to do
    }     
}
