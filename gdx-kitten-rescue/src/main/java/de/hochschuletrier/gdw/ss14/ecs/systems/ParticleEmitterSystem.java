package de.hochschuletrier.gdw.ss14.ecs.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.hochschuletrier.gdw.ss14.ecs.EntityManager;
import de.hochschuletrier.gdw.ss14.ecs.components.ParticleEmitterComponent;

public class ParticleEmitterSystem extends ECSystem {
    
    private TextureRegion[] LiquidParticleTextures;

    public ParticleEmitterSystem(EntityManager entityManager) {
        
        super(entityManager);
        loadParticleTextures();
    }

    @Override
    public void update(float delta) {
        
        Array<Integer> arr = entityManager.getAllEntitiesWithComponents(ParticleEmitterComponent.class);
        
        for (Integer ent : arr) {
            
            
        }
    }

    @Override
    public void render() {
        // Nothing to do
    } 
    
    private void loadParticleTextures() {
        
        Texture tex;
                
        tex = new Texture("data/animations/PartikelBlut.png");
        LiquidParticleTextures = TextureRegion.split(tex, tex.getWidth() / 10, tex.getHeight())[0];
    }
}
