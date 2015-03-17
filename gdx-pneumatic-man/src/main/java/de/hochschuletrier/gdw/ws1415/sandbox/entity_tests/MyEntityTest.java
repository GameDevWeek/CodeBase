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
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;
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

public class MyEntityTest extends SandboxGame {

    private static final Logger logger = LoggerFactory
            .getLogger(EntityTest.class);
    private final PooledEngine engine = new PooledEngine(
            GameConstants.ENTITY_POOL_INITIAL_SIZE,
            GameConstants.ENTITY_POOL_MAX_SIZE,
            GameConstants.COMPONENT_POOL_INITIAL_SIZE,
            GameConstants.COMPONENT_POOL_MAX_SIZE);

    private final HealthSystem Health = new HealthSystem(0);
    private final LimitedSmoothCamera camera;
    private final RenderSystem  renderSystem = new RenderSystem();
    
    public MyEntityTest() {
        engine.addSystem(Health); 
        engine.addSystem(renderSystem);
        
        camera = renderSystem.getCamera();
    }

    @Override
    public void dispose() {
        Main.getInstance().removeScreenListener(camera);
    }

    Entity blockEntity;
    Entity arrow;

    @Override
    public void init(AssetManagerX assetManager) {
        blockEntity = engine.createEntity();       
        
        PositionComponent pc = engine.createComponent(PositionComponent.class);
        pc.reset();
        pc.x = Gdx.graphics.getWidth() * 0.5f;
        pc.y = Gdx.graphics.getHeight() * 0.5f;
        blockEntity.add(pc);
        
        HealthComponent health = engine.createComponent(HealthComponent.class);
        health.reset();
        health.Value = 6;
        logger.info("Health Value: " + health.Value);
        blockEntity.add(health);
        
        blockEntity.add(engine.createComponent(BlockComponent.class));
        
        AnimationComponent animComp = engine.createComponent(AnimationComponent.class);
        animComp.reset();
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
        AnimationExtendedLoader.AnimationExtendedParameter.class);        
        animComp.animation = assetManager.getAnimation("walking");
        blockEntity.add(animComp);
        
        LayerComponent layer = engine.createComponent(LayerComponent.class);
        layer.layer = 1;
        layer.parallax = 0.8f;
        blockEntity.add(layer);
        engine.addEntity(blockEntity); 

        arrow = engine.createEntity();
        DamageComponent Damage = engine.createComponent(DamageComponent.class);
        Damage.damage = 1;
        Damage.damageToTile = true;
        arrow.add(Damage);

        PositionComponent ArrowPosition = engine
                .createComponent(PositionComponent.class);
        ArrowPosition.x = 10;
        ArrowPosition.y = Gdx.graphics.getHeight() * 0.5f;
        arrow.add(ArrowPosition);
        LayerComponent arrowLayer = engine.createComponent(LayerComponent.class);;
        arrowLayer.layer = 0;
        arrowLayer.parallax = 1f;
        
        arrow.add(arrowLayer);
        
        arrow.add(animComp);
        
        engine.addEntity(arrow);
        
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setBounds(0, 0, 1000, 1000);
        camera.updateForced();
        
        Main.getInstance().addScreenListener(camera);
    }

    public void DrawRect(float x, float y, float dw, float dh, Color color) {
        DrawUtil.fillRect(x - dw, y - dh, dw * 2.0f, dh * 2.0f, color);
    }

    @Override
    public void update(float delta) {
    	camera.bind();
    	camera.update(delta);
        engine.update(delta);
        

        float DH = 100.0f;
        float DW = 100.0f;

        PositionComponent arrowPosition = arrow
                .getComponent(PositionComponent.class);

        arrowPosition.x += 100 * delta;

        HealthComponent healthOfBlock = blockEntity
                .getComponent(HealthComponent.class);
//        DrawRect(blockEntity.getComponent(PositionComponent.class).x, blockEntity.getComponent(PositionComponent.class).y, DW * 0.5f, DH * 0.5f, new Color(
//                healthOfBlock.Value / 10.0f, 1.0f, 1.0f, 1.0f));
//        DrawRect(arrowPosition.x, arrowPosition.y, 10, 10, Color.RED);

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && healthOfBlock.Value > 0) {
        	healthOfBlock.Value -= 1; 
        }
        
        float X = blockEntity.getComponent(PositionComponent.class).x;
         
	    if (arrow.getComponent(PositionComponent.class).x > X - 50
	            && arrow.getComponent(PositionComponent.class).x < X + 50) {
	        DamageComponent arrowDamage = arrow
	                .getComponent(DamageComponent.class);
	        if (arrowDamage.damageToTile) {
	            healthOfBlock.Value -= arrowDamage.damage;
	        }
	    }
    }
}

