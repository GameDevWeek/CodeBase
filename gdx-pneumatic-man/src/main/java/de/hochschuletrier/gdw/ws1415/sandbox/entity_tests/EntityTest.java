package de.hochschuletrier.gdw.ws1415.sandbox.entity_tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.BlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.HealthSystem;
import de.hochschuletrier.gdw.ws1415.game.systems.RenderSystem;
import de.hochschuletrier.gdw.ws1415.sandbox.SandboxGame;

public class EntityTest extends SandboxGame {

    private static final Logger logger = LoggerFactory
            .getLogger(EntityTest.class);
    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE,
            GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE,
            GameConstants.COMPONENT_POOL_MAX_SIZE);

    private final HealthSystem Health = new HealthSystem(0);
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private final RenderSystem  DestructableBlockRender = new RenderSystem();

    public EntityTest() {
        engine.addSystem(Health); 
        engine.addSystem(DestructableBlockRender);
    }

    @Override
    public void dispose() {

    }

    Entity BlockEntity;
    Entity Arrow;
    Entity Unit;

    @Override
    public void init(AssetManagerX assetManager) {
       
        
        BlockEntity = engine.createEntity();
        
        
        PositionComponent pc = engine.createComponent(PositionComponent.class);
        pc.reset();
        pc.x = Gdx.graphics.getWidth() * 0.5f;
        pc.y = Gdx.graphics.getHeight() * 0.5f;
        BlockEntity.add(pc);
        
        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.reset();
        Health.Value = 6;
        logger.info("Health Value: " + Health.Value);
        BlockEntity.add(Health);
        
        BlockEntity.add(engine.createComponent(BlockComponent.class));
        
        AnimationComponent ac = engine.createComponent(AnimationComponent.class);
        ac.reset();
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
        AnimationExtendedLoader.AnimationExtendedParameter.class);        
        ac.animation = assetManager.getAnimation("walking");
        BlockEntity.add(ac);
        
        BlockEntity.add(engine.createComponent(LayerComponent.class));
        engine.addEntity(BlockEntity); 

        Arrow = engine.createEntity();
        DamageComponent Damage = engine.createComponent(DamageComponent.class);
        Damage.damage = 1;
        Damage.damageToTile = true;
        Arrow.add(Damage);

        PositionComponent ArrowPosition = engine
                .createComponent(PositionComponent.class);
        ArrowPosition.x = 10;
        ArrowPosition.y = Gdx.graphics.getHeight() * 0.5f;
        Arrow.add(ArrowPosition);
        engine.addEntity(Arrow);

        Unit = engine.createEntity();
        PositionComponent UnitPosition = engine.createComponent(PositionComponent.class);
        UnitPosition.x = 50;
        UnitPosition.y = 50;
        Unit.add(UnitPosition);
        engine.addEntity(Unit);
        
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setBounds(0, 0, 1000, 1000);
        camera.updateForced();
    }

    public void DrawRect(float x, float y, float dw, float dh, Color color) {
        DrawUtil.fillRect(x - dw, y - dh, dw * 2.0f, dh * 2.0f, color);
    }

    float unitSpeed = 1;
    
    @Override
    public void update(float delta) {
        engine.update(delta);
        camera.update(delta);

        float DH = 100.0f;
        float DW = 100.0f;

        PositionComponent ArrowPosition = Arrow
                .getComponent(PositionComponent.class);

        ArrowPosition.x += 100 * delta;
        
        PositionComponent UnitPosition = Unit.getComponent(PositionComponent.class);
        
        UnitPosition.x += unitSpeed*(100 * delta);
        
        if(UnitPosition.x <= 50)
        	unitSpeed = 1;
        
        if(UnitPosition.x >= 250)
        	unitSpeed = -1;
        
        DrawRect(UnitPosition.x, UnitPosition.y, 10, 10, Color.GREEN);
               
        HealthComponent HealthOfBlock = BlockEntity
                .getComponent(HealthComponent.class);
        DrawRect(BlockEntity.getComponent(PositionComponent.class).x, BlockEntity.getComponent(PositionComponent.class).y, DW * 0.5f, DH * 0.5f, new Color(
                HealthOfBlock.Value / 10.0f, 1.0f, 1.0f, 1.0f));
      //  DrawRect(ArrowPosition.x, ArrowPosition.y, 10, 10, Color.RED);

        DestructableBlockRender.update(delta);
          if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && HealthOfBlock.Value > 0) {
          HealthOfBlock.Value -= 1; }
         
        /*if (Arrow.getComponent(PositionComponent.class).x > X - 50
                && Arrow.getComponent(PositionComponent.class).x < X + 50) {
            DamageComponent ArrowDamage = Arrow
                    .getComponent(DamageComponent.class);
            if (ArrowDamage.damageToTile) {
                HealthOfBlock.Value -= ArrowDamage.damage;
            }
        }*/
    }
}
