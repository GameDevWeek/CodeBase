package de.hochschuletrier.gdw.ws1415.sandbox.entity_tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.HealthSystem;
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

    public EntityTest() {
        engine.addSystem(Health);
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
        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.reset();
        logger.info("Health Value: " + Health.Value);
        BlockEntity.add(Health);
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

        float X = Gdx.graphics.getWidth() * 0.5f;
        float Y = Gdx.graphics.getHeight() * 0.5f;
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
        DrawRect(X, Y, DW * 0.5f, DH * 0.5f, new Color(
                HealthOfBlock.Value / 10.0f, 1.0f, 1.0f, 1.0f));
        DrawRect(ArrowPosition.x, ArrowPosition.y, 10, 10, Color.RED);

        /*
         * if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
         * HealthOfBlock.Value -= 1; }
         */
        if (Arrow.getComponent(PositionComponent.class).x > X - 50
                && Arrow.getComponent(PositionComponent.class).x < X + 50) {
            DamageComponent ArrowDamage = Arrow
                    .getComponent(DamageComponent.class);
            if (ArrowDamage.damageToTile) {
                HealthOfBlock.Value -= ArrowDamage.damage;
            }
        }
    }
}
